(ns pinkgorilla.notebook-ui.sniffer.dump
  (:require
   [re-frame.core :as rf]
   [taoensso.timbre :refer-macros [trace debug debugf info infof error]]
   [webly.ws.core :refer [send!]]
   ;[webly.ws.msg-handler :refer [-event-msg-handler]]
   ))

(defn dump [msg]
  (infof "sniffer/dump %s " msg)
  (try
    (send! [:goldly/dump msg]) ; 5000 (fn [data] ; [event-type data]]
                      ;           (info "send data:" data)
                      ;           (dispatch [:goldly/event goldly-tag data])))
    (catch :default e
      (error "exception sending to clj: " e)))
  (info "sniffer/dump done."))

(rf/reg-event-fx
 :sniffer/dump ; send data to clj.
 (fn [cofx [_ msg]]
   (dump msg)
   nil))