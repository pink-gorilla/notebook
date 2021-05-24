(ns pinkgorilla.notebook-ui.events.events
  (:require
   [taoensso.timbre :as timbre :refer-macros [info error]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]))

(reg-event-db
 :notebook/activate!
 (fn [db [_ storage]]
   (info "setting current storage: " storage)
   (assoc-in db [:notebook] storage)))







