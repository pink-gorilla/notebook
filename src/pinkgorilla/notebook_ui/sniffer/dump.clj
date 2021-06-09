(ns pinkgorilla.notebook-ui.sniffer.dump
  (:require
   [taoensso.timbre :as timbre :refer [debug info errorf debug warn error]]
   [webly.ws.msg-handler :refer [-event-msg-handler]]))

(defmethod -event-msg-handler :sniffer/dump
  [{:as ev-msg :keys [event]}]
  (let [[event-name data] event]
    ;(debug "sniffer: " data)
    (spit ".sniffer.edn" (str "\r\n" (pr-str data)) :append true)))
