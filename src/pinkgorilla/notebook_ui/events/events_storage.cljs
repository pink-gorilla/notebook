(ns pinkgorilla.notebook-ui.events.events-storage
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :as rf]))


;; insert


(rf/reg-event-db
 :notebook/save
 (fn [db _]
   (let [storage (:notebook db)]
     (rf/dispatch [:document/save storage])
     db)))


(rf/reg-event-db
 :notebook/activate!
 (fn [db [_ storage]]
   (info "setting current storage: " storage)
   (assoc-in db [:notebook] storage)))

