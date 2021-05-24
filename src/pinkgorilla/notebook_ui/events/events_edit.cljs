(ns pinkgorilla.notebook-ui.events.events-edit
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db dispatch]]))


;; insert


(reg-event-db
 :notebook/save
 (fn [db _]
   (let [storage (:notebook db)]
     (dispatch [:document/save storage])
     db)))


