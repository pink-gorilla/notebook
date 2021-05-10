(ns pinkgorilla.notebook-ui.events.events-eval
  (:require
   [taoensso.timbre :as timbre :refer-macros [info infof errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
   [pinkgorilla.notebook-ui.hydration :refer [code-segment-ids]]
   #_[pinkgorilla.nrepl.op.eval :refer [nrepl-eval]]))

;; Evaluation

(defn- eval-segment! [db storage segment-id]
  (let [document (get-in db [:document :documents storage])
        segment (get-in document [:segments segment-id])
        {:keys [kernel id code]} segment]
    (if code
      (dispatch [:kernel/eval kernel code [:document :documents storage :segments segment-id]])
      (errorf "cannot eval segment %s - code is nil!" segment-id))))

(reg-event-fx
 :segment-active/eval
 (fn [{:keys [db] :as cofx} [_]]
   (let [storage (:notebook db)
         document (get-in db [:document :documents storage])
         segment-id (:active document)]
     (dispatch [:segment-active/clear])
     (eval-segment! db storage segment-id)
     {})))

(defn code? [segment]
  (= :code (:type segment)))

(reg-event-db
 :notebook/evaluate-all
 (fn [db _]
   (dispatch [:notebook/clear-all])
   (let [storage (:notebook db)
         document (get-in db [:document :documents storage])
         {:keys [queued] :or {queued []}} document
         queued-add (code-segment-ids document)]
     (info "notebook - evaluate all..")
     (dispatch [:notebook/evaluate-next-queued])
     (-> db
         (assoc-in [:document :documents storage :queued] (concat queued queued-add))))))

(reg-event-db
 :notebook/evaluate-next-queued
 (fn [db _]
   ; evals the next queued-next segment 
   (let [storage (:notebook db)
         document (get-in db [:document :documents storage])
         {:keys [queued]
          :or {queued []}} document
         queued-next (first queued)]
     (if queued-next
       (do
         (info "evaluating next queued segment " queued-next)
         (eval-segment! db storage queued-next)
         (assoc-in db [:document :documents storage :queued] (rest queued)))
       (do
         (infof "no more queued segments: %s" queued)
         db)))))
