(ns pinkgorilla.events.message
  (:require
   [re-frame.core :refer [reg-event-db dispatch]]
   [pinkgorilla.events.helper :refer [standard-interceptors]]))

(defn display-message
  [db [_ message timeout]]
  (when timeout
    (js/setTimeout #(dispatch [:display-message nil]) timeout))
  (assoc-in db [:message] message))

(reg-event-db
 :display-message
 [standard-interceptors]
 display-message)




