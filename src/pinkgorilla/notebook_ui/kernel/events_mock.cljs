(ns pinkgorilla.notebook-ui.kernel.events-mock
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [reg-event-fx dispatch]]))

(defn mock-result [kernel]
  {:err (str "Kernel " kernel " not found!")})

(reg-event-fx
 :mock/eval
 (fn [_ [_  kernel code result-db-path]]
   (dispatch [:kernel/save-result result-db-path true (mock-result kernel)])))