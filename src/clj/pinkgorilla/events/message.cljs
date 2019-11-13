(ns pinkgorilla.events.message
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))


(defn display-message
  [db [_ message timeout]]
  (if timeout
    (js/setTimeout #(dispatch [:display-message nil]) timeout))
  (merge db {:message message}))

(reg-event-db
 :display-message
 [standard-interceptors]
 display-message)

(reg-event-db
 :process-error-response
 (fn [db [_ response]]
   (display-message db [:process-error-response (str "Ugh, got bad response : "
                                                     (:status-text response) " ("
                                                     (:status response) ")")])))
