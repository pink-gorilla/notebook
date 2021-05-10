(ns pinkgorilla.notebook-ui.nrepl.events.info
  (:require
   [re-frame.core :refer [reg-event-fx dispatch]]
   [pinkgorilla.nrepl.client.core :refer [op-describe op-lssessions op-lsmiddleware]]))

(reg-event-fx
 :nrepl/describe
 (fn [cofx [_]]
   (dispatch [:nrepl/op-db (op-describe) [:nrepl :info :describe]])))

(reg-event-fx
 :nrepl/ls-sessions
 (fn [cofx [_]]
   (dispatch [:nrepl/op-db (op-lssessions) [:nrepl :info]])))

(reg-event-fx
 :nrepl/ls-middleware
 (fn [cofx [_]]
   (dispatch [:nrepl/op-db (op-lsmiddleware) [:nrepl :info]])))

(reg-event-fx
 :nrepl/sniffer-status
 (fn [cofx [_]]
   (dispatch [:nrepl/op-db {:op "sniffer-status"} [:nrepl :info]])))

(reg-event-fx
 :nrepl/info-get
 (fn [cofx [_]]
   (dispatch [:nrepl/describe])
   (dispatch [:nrepl/ls-sessions])
   (dispatch [:nrepl/ls-middleware])
   (dispatch [:nrepl/sniffer-status])
   (dispatch [:nrepl/eval "(+ 1 1)" [:nrepl :info :eval-test]])))




