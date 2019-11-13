(ns pinkgorilla.events.kernel
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   ))

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
   (let [segment (get-in db [:worksheet :segments seg-id])]
     (assoc-in db [:worksheet :segments seg-id] (merge segment response)))))

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
     (if (= active-id seg-id)
       (if (= (- seg-count 1) (.indexOf segment-order active-id))
         (dispatch [:worksheet:newBelow])
         (dispatch [:worksheet:leaveForward])))
     (assoc-in db [:worksheet :queued-code-segments] (-> (remove #(= seg-id %) queued-segs)
                                                         set)))))

(reg-event-db
 :output-error
 (fn [db [_ seg-id e]]
    ;; TODO Should probably write to output cell
    ;; (js-debugger)
   (js/alert (.-message e) "for cell " seg-id)
   db))