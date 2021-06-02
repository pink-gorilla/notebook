(ns pinkgorilla.notebook-ui.sniffer.dump
  (:require
   [taoensso.timbre :as timbre :refer [info errorf debug warn error]]

   [webly.ws.core :refer [send-all! send! send-response on-conn-chg]]
   [webly.ws.msg-handler :refer [-event-msg-handler]]))

(defmethod -event-msg-handler :sniffer/dump
  [{:as ev-msg :keys [event]}]
  (let [[event-name data] event]
    (info "sniffer: " data)))
