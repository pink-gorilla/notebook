(ns pinkgorilla.notebook-ui.nrepl.events.eval
  (:require
   [taoensso.timbre :refer-macros [infof]]
   [re-frame.core :refer [reg-event-fx dispatch]]
   [pinkgorilla.nrepl.client.core :refer [op-eval]]))

(reg-event-fx
 :nrepl/eval
 (fn [_ [_ code result-db-path]]
   (infof ":nrepl/eval code: code db-path: %s"  code result-db-path)
   (dispatch [:nrepl/op-dispatch
              (op-eval code)
              [:kernel/save-result result-db-path true]])))

