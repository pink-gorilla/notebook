(ns pinkgorilla.events.settings
  "events related to the settings dialog"
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   ))

(reg-event-db
 :app:showsettings
 (fn [db [_ doc]]
   (assoc-in db [:settings] {:show true})))

(reg-event-db
 :app:hide-settings
 (fn [db _]
   (assoc-in db [:settings] {:show false})))

