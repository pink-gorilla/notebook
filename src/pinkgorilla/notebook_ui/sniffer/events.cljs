(ns pinkgorilla.notebook-ui.sniffer.events
  (:require
   [taoensso.timbre :as timbre :refer-macros [info errorf debug warn error]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.sniffer.dump :refer [dump]]
   [pinkgorilla.nrepl.client.op.eval :refer [process-fragment initial-value]]
   ))


(rf/reg-event-fx
 :nrepl/register-sniffer-sink
 (fn [{:keys [db] :as cofx}  [_]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (info "registering sniffer-sink")
     ;(dispatch [:nrepl/op-dispatch-rolling {:op "eval" :code "(+ 7 7)"} [:sniffer/rcvd]])
     (rf/dispatch [:nrepl/op-dispatch-rolling {:op "sniffer-sink"} [:sniffer/rcvd]])
     nil)))


#_(defn process [msg]
  (let [[path notebook] (get-notebook db)]
    (cond
      (= "eval" (:op msg))
      (assoc-in db path (add-code-segment notebook msg))
      :else
      (assoc-in db path (add-result notebook msg))))
  )


(rf/reg-event-db
 :sniffer/rcvd
 (fn [db [_ msg]]
   (info "sniffer rcvd: " msg)
   (let [{:keys [session-id-sink session-id-source]} msg]
     (if (or session-id-sink session-id-source)
       ; admin message
       db
       ; eval or eval result
       (do ;(process msg)
           (dump msg)
           db
           )
       ))))


