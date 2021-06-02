(ns pinkgorilla.notebook-ui.app.events
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :as rf]
   ;[pinkgorilla.notebook-ui.hydration :refer [hydrate dehydrate]]
   [pinkgorilla.notebook-ui.app.css :as notebook-css]))

(rf/dispatch [:css/add-components notebook-css/components notebook-css/config])

(rf/reg-event-db
 :notebook/start
 (fn [db [_]]
   (let [db (or db {})
         {:keys [nrepl-endpoint]} (:config db)]
     (info "notebook/start")
     (rf/dispatch [:ga/event {:category "notebook" :action "started" :label 77 :value 13}])

     ; explorer
     (rf/dispatch [:explorer/init  {;:fn-hydrate nil ; hydrate
                                    ;:fn-dehydrate nil ; dehydrate
                                    }])

     ; nrepl
     (rf/dispatch [:nrepl/init nrepl-endpoint])

     ;(dispatch [:sniffer/init]) ; hack so sniffer notebook route works at startup.
     (rf/dispatch [:punk/init])
     (rf/dispatch [:completion/init])
     (rf/dispatch [:schema/check-on])

     (rf/dispatch [:webly/status :running])

     db)))

(rf/reg-event-db
 :webly/before-load
 (fn [db [_]]
   (info "gorilla-notebook reload..")

   db))

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
