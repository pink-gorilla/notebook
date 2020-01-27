(ns pinkgorilla.events.kernel
  "Process results from the kernel and update notebook segments in app-db"
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :refer [reg-event-db dispatch]]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   [pinkgorilla.util :refer [application-url ws-origin]]
   [pinkgorilla.kernel.nrepl :refer [start-repl!]]))

(reg-event-db
 :evaluator:value-response
 (fn [db [_ seg-id response ns]]
   (let [segment (get-in db [:worksheet :segments seg-id])]
     (-> db
         (assoc-in [:worksheet :ns] ns)
         (assoc-in [:worksheet :segments seg-id] (merge segment response))))))

(reg-event-db
 :evaluator:console-response
 (fn [db [_ seg-id response]]
   (let [;; segment (get-in db [:worksheet :segments seg-id])
         _ (info "console response received: " response)]
     ;(assoc-in db [:worksheet :segments seg-id] (merge segment response))
     (update-in db [:worksheet :segments seg-id :console-response] str (:console-response response)))))

(reg-event-db
 :evaluator:error-response
  ;; TODO: We should get the seg-id here instead of grabbing it from the details
 (fn [db [_ error-details]]
   (let [segment-id (:segment-id error-details)
         segment (get-in db [:worksheet :segments segment-id])]
     (assoc-in db [:worksheet :segments segment-id] (merge segment error-details)))))

(reg-event-db
 :evaluator:done-response
 (fn [db [_ seg-id]]
   (let [segment-order (get-in db [:worksheet :segment-order])
         seg-count (count segment-order)
         active-id (get-in db [:worksheet :active-segment])
         queued-segs (get-in db [:worksheet :queued-code-segments])]
     (when (= active-id seg-id)
       (if (= (- seg-count 1) (.indexOf segment-order active-id))
         (dispatch [:worksheet:newBelow])
         (dispatch [:worksheet:leaveForward])))
     (assoc-in db [:worksheet :queued-code-segments] (-> (remove #(= seg-id %) queued-segs)
                                                         set)))))

(reg-event-db
 :output-error
 (fn [db [_ seg-id e]]
    ;; TODO Should probably write to output cell
   (js/alert (.-message e) "for cell " seg-id)
   db))

(reg-event-db
 :kernel-clj-status-set
 (fn [db [_ connected session-id]]
   (-> db
       (assoc-in [:kernel-clj :connected] connected)
       (assoc-in [:kernel-clj :session-id] session-id))))

(reg-event-db
 :kernel-clj-connect
 (fn [db [_]]
   (start-repl! (ws-origin "repl/" (application-url)))
   db))
