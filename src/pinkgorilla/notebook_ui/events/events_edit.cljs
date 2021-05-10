(ns pinkgorilla.notebook-ui.events.events-edit
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db dispatch]]
   [pinkgorilla.notebook-ui.hydration  :refer [clear-all clear-active
                                               remove-segment insert-segment-at create-code-segment
                                               toggle-view-segment]]
   [pinkgorilla.notebook-ui.events.events :refer [notebook-op]]
   [pinkgorilla.notebook-ui.events.events-segment :refer [segment-op]]))

;; input

(reg-event-db
 :segment/set-md
 (fn [db [_ seg-id md]]
   (notebook-op db #(assoc-in % [:segments seg-id :md] md))))

(reg-event-db
 :segment/set-code
 (fn [db [_ seg-id code]]
   (notebook-op db
                #(assoc-in % [:segments seg-id :code] code))))

(reg-event-db
 :segment/set-result
 (fn [db [_ seg-id result]]
   (notebook-op db #(assoc-in % [:segments seg-id :result] result))))

;; output

(reg-event-db
 :segment-active/clear
 (fn [db [_]]
   (info "clear active")
   (notebook-op
    db
    clear-active)))

(reg-event-db
 :notebook/clear-all
 (fn [db [_]]
   (info "clear all")
   (notebook-op
    db
    clear-all)))

;; kernel

(defn kernel-toggle [{:keys [kernel] :as segment}]
  (assoc segment :kernel
         (case kernel
           :clj :cljs
           :cljs :clj
           :clj)))

(reg-event-db
 :segment-active/kernel-toggle
 (fn [db [_]]
   (segment-op db kernel-toggle)))

(reg-event-db
 :segment-active/toggle-view
 (fn [db [_]]
   (segment-op db toggle-view-segment)))



; edit?


(reg-event-db
 :notebook/set-cm-md-edit
 (fn [db [_ edit?]]
   (assoc-in db [:notebook/edit?] edit?)))

(reg-event-db
 :notebook/close-dialog-or-exit-edit
 (fn [db [_]]
   (let [show? (get-in db [:modal :show?])]
     (if show?
       (dispatch [:modal/close])
       (dispatch [:notebook/set-cm-md-edit false]))
     db)))

;; delete

(defn remove-active-segment [notebook]
  (remove-segment notebook (:active notebook)))

(reg-event-db
 :segment-active/delete
 (fn [db _]
   (info "clear all")
   (notebook-op
    db
    remove-active-segment)))

;; insert

(defn insert-segment
  [index-fn notebook]
  (let [{:keys [active order]} notebook
        active-idx (.indexOf order active)
        new-segment (create-code-segment "")]
    (insert-segment-at notebook (index-fn active-idx) new-segment)))

(reg-event-db
 :segment/new-above
 (fn [db _]
   (notebook-op
    db
    (partial insert-segment identity))))

(reg-event-db
 :segment/new-below
 (fn [db _]
   (notebook-op
    db
    (partial insert-segment inc))))

(reg-event-db
 :notebook/save
 (fn [db _]
   (let [storage (:notebook db)]
     (dispatch [:document/save storage])
     db)))


