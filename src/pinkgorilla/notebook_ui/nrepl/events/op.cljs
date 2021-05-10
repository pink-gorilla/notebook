(ns pinkgorilla.notebook-ui.nrepl.events.op
  (:require
   [taoensso.timbre :refer-macros [trace debug info infof error errorf]]
   [cljs.core.async :as async :refer [<!] :refer-macros [go go-loop]]
   [re-frame.core :refer [reg-event-fx reg-event-db dispatch]]
   [pinkgorilla.nrepl.client.core :refer [send-request!]]))

(defn- run-op-dispatch
  "makes a nrepl request
   pinkgorilla.nrepl.client uses core.async.
   run-op-dispatch bridges core.async with reframe
   te result of the nrepl request is dispatched to reframe
   with the result as its only argument"
  [conn op handler]
  (go
    (let [r (<! (send-request! conn op))
          d (into handler [r])]
      (infof "nrepl/run-op-dispatch: %s result: %s " d r)
      (dispatch d))))

; runs nrepl operation op-fn with args op-args.
; calls reframe dispatch with result
(reg-event-fx
 :nrepl/op-dispatch
 (fn [{:keys [db] :as cofx}  [_ op handler]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (if conn
       (run-op-dispatch conn op handler)
       (error "cannot run nrepl op - not connected!"))
     ;cofx
     nil)))

; an internal function really
(reg-event-db
 :nrepl/internal-op-db-save-result
 (fn [db [_ db-path result]]
   (infof ":nrepl/internal-op-db-save-result db-path: %s result: %s" db-path result)
   (let [prior (get-in db db-path)
         result-merged (merge prior result)]
     (assoc-in db db-path result-merged))))

; runs nrepl operation op-fn with args op-args.
; stores result in app-db with path db-path
(reg-event-fx
 :nrepl/op-db
 (fn
   [cofx  [_ op db-path]]
   (if (nil? db-path)
     (error ":nrepl/op-db path nil. not running nrepl op.")
     (dispatch [:nrepl/op-dispatch op [:nrepl/internal-op-db-save-result db-path]]))
     ;cofx
   nil))



;; 


(defn- run-op-dispatch-rolling
  "makes a nrepl request
   pinkgorilla.nrepl.client uses core.async.
   run-op-dispatch bridges core.async with reframe
   te result of the nrepl request is dispatched to reframe
   with the result as its only argument"
  [conn op handler]
  (if-let [result-ch (send-request! conn op true)]
    (go-loop [r (<! result-ch)]
      (when r ; when chan closes, nil is returned -> exit go-loop 
        (let [d (into handler [r])]
          (infof "nrepl/run-op-dispatch-rolling: %s" d)
          (dispatch d))
        (recur (<! result-ch))))
    (info "cannot send nrepl msg. not connected!")))

(reg-event-fx
 :nrepl/op-dispatch-rolling
 (fn [{:keys [db] :as cofx}  [_ op handler]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (run-op-dispatch-rolling conn op handler)
     ;cofx
     nil)))





