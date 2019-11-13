(ns pinkgorilla.events.worksheet
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [day8.re-frame.undo :as undo :refer [undoable]]
   [pinkgorilla.prefs :as prefs]
   [pinkgorilla.db :as db :refer [initial-db]]
   [pinkgorilla.notebook.core :refer [save-notebook-hydrated
                                      to-code-segment to-free-segment
                                      remove-segment insert-segment-at
                                      create-code-segment]]
   [pinkgorilla.editor.core :as editor]
   [pinkgorilla.kernel.core :as kernel]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler check-and-throw standard-interceptors]]
   [taoensso.timbre :refer-macros (info)]))


(defn change-to
  [db change-fn]
  (if-let [active-id (get-in db [:worksheet :active-segment])]
    (assoc-in db
              [:worksheet :segments active-id]
              (change-fn (get-in db [:worksheet :segments active-id])))
    db))

(reg-event-db
 :worksheet:changeToCode
 [standard-interceptors]
 (fn [db _]
   (change-to db to-code-segment)))

(reg-event-db
 :worksheet:changeToFree
 [standard-interceptors]
 (fn [db _]
   (change-to db to-free-segment)))

(reg-event-db
 :worksheet:clear-all-output
 [standard-interceptors]
 (fn [db _]
   (assoc-in db
             [:worksheet :segments]
             (into {}
                   (for [[k v] (get-in db [:worksheet :segments])]
                     [k (apply dissoc v [:console-response :value-response :error-text :exception])])))))

(reg-event-db
 :worksheet:clear-output
 [standard-interceptors]
 (fn [db _]
   (let [active-id (get-in db [:worksheet :active-segment])]
     (update-in db [:worksheet :segments active-id]
                #(apply dissoc % [:console-response :value-response :error-text :exception])))))

(reg-event-db
 :worksheet:completions
 [standard-interceptors]
 (fn [db _]
   (let [active-id (get-in db [:worksheet :active-segment])
         active-segment (get-in db [:worksheet :segments active-id])
          ;;kernel (:kernel active-segment)
         ]
     (if (= :code (:type active-segment))
       (editor/complete active-id (get-in db [:worksheet :ns])))
     db)))

(reg-event-db
 :worksheet:delete
 [(conj standard-interceptors (undoable "Delete segment"))]
 (fn [db _]
   (let [worksheet (get db :worksheet)
         active-id (get-in db [:worksheet :active-segment])]
     (assoc db :worksheet (remove-segment worksheet active-id)))))

(reg-event-db
 :worksheet:deleteBackspace
 [standard-interceptors]
 (fn [db _]
   (let [worksheet (get db :worksheet)
         active-id (get-in db [:worksheet :active-segment])]
     (assoc db :worksheet (remove-segment worksheet active-id)))))

(reg-event-db
 :worksheet:evaluate
 [standard-interceptors]
 (fn [db _]
   (let [active-id (get-in db [:worksheet :active-segment])
         active-segment (get-in db [:worksheet :segments active-id])
         new-active-segment (merge active-segment {:console-response nil
                                                   :value-response nil
                                                   :error-text     nil
                                                   :exception      nil})
         kernel (:kernel active-segment)
         queued-segs (get-in db [:worksheet :queued-code-segments])]
     (kernel/send-eval-message! kernel active-id (get-in active-segment [:content :value]))
     (-> (assoc-in db [:worksheet :segments active-id] new-active-segment)
         (assoc-in [:worksheet :queued-code-segments] (conj queued-segs (:id new-active-segment)))))))

(reg-event-db
 :worksheet:evaluate-all
 [standard-interceptors]
 (fn [db _]
   (let [segments (get-in db [:worksheet :segments])
         segment-order (get-in db [:worksheet :segment-order])
         sorted-code-segments (->> (map #(% segments) segment-order)
                                   (filter (fn [segment] (= :code (:type segment)))))]
     (doall (map #(kernel/send-eval-message!
                   (get-in % [:kernel])
                   (:id %)
                   (get-in % [:content :value])) sorted-code-segments))
     (assoc-in db [:worksheet :queued-code-segments] (-> (map #(:id %) sorted-code-segments)
                                                         set)))))

(defn leave-active
  [db next-fn]
  (let [segment-order (get-in db [:worksheet :segment-order])
        active-id (get-in db [:worksheet :active-segment])
        next-active-idx (next-fn (.indexOf segment-order active-id))
        next-active-id (if (> next-active-idx -1) (nth segment-order next-active-idx))]
    (if next-active-id
      (assoc-in db [:worksheet :active-segment] next-active-id)
      db)))

(reg-event-db
 :worksheet:leaveBack
 [standard-interceptors]
 (fn [db _]
   (leave-active db dec)))

(reg-event-db
 :worksheet:leaveForward
 [standard-interceptors]
 (fn [db _]
   (leave-active db inc)))


(defn move-active
  [db next-fn]
  (let [segment-order (get-in db [:worksheet :segment-order])
        active-id (get-in db [:worksheet :active-segment])
        active-idx (.indexOf segment-order active-id)
        next-idx (next-fn active-idx)]
    (if (get segment-order next-idx)
      (->> (db/swap segment-order active-idx next-idx)
           (assoc-in db [:worksheet :segment-order]))
      db)))

;; TODO alt+g alt+d works only once for me, might be intercepted by browser
;; or window manager
(reg-event-db
 :worksheet:moveDown
 [standard-interceptors]
 (fn [db _]
   (move-active db inc)))

(reg-event-db
 :worksheet:moveUp
 [standard-interceptors]
 (fn [db _]
   (move-active db dec)))

(defn insert-segment
  [index-fn db _]
  (let [segment-order (get-in db [:worksheet :segment-order])
        active-id (get-in db [:worksheet :active-segment])
        active-idx (.indexOf segment-order active-id)
        new-segment (create-code-segment "")]
    (merge db {:worksheet
               (-> (:worksheet db)
                   (insert-segment-at (index-fn active-idx) new-segment))})))

(reg-event-db
 :worksheet:newAbove
 [(conj standard-interceptors (undoable "Insert segment"))]
 (partial insert-segment identity))

(reg-event-db
 :worksheet:newBelow
 [(conj standard-interceptors (undoable "Insert segment"))]
 (partial insert-segment inc))

;; Using re-frame undo instead
#_(reg-event-db
   :worksheet:undelete
   [standard-interceptors]
   (fn [db _]
     db))
