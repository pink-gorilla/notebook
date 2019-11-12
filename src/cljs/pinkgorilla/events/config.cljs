(ns pinkgorilla.events.config
  "events related to ???"
  (:require
   [ajax.core :as ajax :refer [GET POST]]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
    [pinkgorilla.db :as db :refer [initial-db]]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   ))

(def install-commands
  (re-frame.core/->interceptor
   :id :install-commands
   :after (fn
            [context]
            (db/install-commands (get-in context [:coeffects :db :all-commands]))
            context)))

(reg-event-db
 :process-config-response
 [install-commands]
 (fn [db [_ response]]
   (-> (assoc-in db [:config] response)
       (assoc :message nil))))


(reg-event-fx                                               ;; note the trailing -fx
 :initialize-config
 (fn [{:keys [db]} _]
   {:db         (merge db {:message "Loading configuration ..."})
    :http-xhrio {:method          :get
                 :uri             (str (:base-path db) "config")
                 :timeout         5000                     ;; optional see API docs
                 :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:process-config-response]
                 :on-failure      [:process-error-response]}}))

