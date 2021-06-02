(ns demo.app
  (:require
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre :refer [info warn]]
   [webly.user.app.app :refer [webly-run!]]
   ; side-effects
   ;[pinkgorilla.ui.default-renderer] ; add ui renderer definitions 
   [pinkgorilla.notebook-ui.app.app]
   [demo.pages.examples]
   [demo.routes :refer [routes-api routes-app]]))

(defn ^:export start []
  (webly-run! routes-api routes-app))

(rf/reg-event-db
 :webly/before-load
 (fn [db [_]]
   (warn "webly/before-load: customize your app for lein webly watch..")
   db))

(rf/reg-event-db
 :notebook/start
 (fn [db [_]]
   (info "notebook-init")
   (rf/dispatch [:notebook/init [:webly/status :running]])
   db))