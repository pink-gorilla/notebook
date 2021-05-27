(ns pinkgorilla.notebook-ui.kernel.events
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [re-frame.core :refer [reg-event-fx reg-event-db dispatch]]
   [pinkgorilla.nrepl.client.core :refer [op-stacktrace]]))




(reg-event-fx
 :kernel/eval
 (fn [_ [_ kernel code result-db-path]]
   ;(let [msg-type (kernel->msg-type kernel)]
   (dispatch [:nrepl/eval code result-db-path])
     ;(dispatch [msg-type kernel code result-db-path])
     ;)
   ))

#_(reg-event-db
 :kernel/save-result
 (fn [db [_ db-path eval? data]]
   (if (nil? db-path)
     (do
       (error ":kernel/save-result path nil. not saving data: " data)
       db)
     (let [segment (get-in db db-path)
           segment-updated (merge segment data)
           ns-path  (conj  (into [] (take 3 db-path)) :ns)
           _ (info "nrepl result save to: " db-path " data: " data)
           x (-> db
                 (assoc-in db-path segment-updated)
                 (assoc-in ns-path (:ns data)))]
     ;(info "seg: " (get-in db path))
     ;(info "db: " (get-in x [:document :documents]))
       (when (:err data)
         (when (first (:err data))
           (info "getting stacktrace of exception: " (first (:err data)))
           (dispatch [:kernel/stacktrace db-path])))
       (when eval?
         (info ":kernel/save-result - dispatching :notebook/evaluate-next-queued")
         (dispatch [:notebook/evaluate-next-queued]))
       x))))

; :nrepl/stacktrace

#_ (reg-event-fx
 :kernel/stacktrace
 (fn [cofx [_ path]]
   (info "getting stacktrace for path:" path)
   (dispatch [:nrepl/op-db (op-stacktrace) path])
   nil))
