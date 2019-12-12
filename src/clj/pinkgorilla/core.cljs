(ns pinkgorilla.core
  (:require
   [clojure.string :as str]
   ;; [taoensso.timbre :refer-macros [info]]
   [secretary.core :as secretary]
   [reagent.core :as ra]
   [re-frame.core :refer [dispatch-sync]]
   ;; [pinkgorilla.events.usage-analytics]
   [pinkgorilla.subs] ; bring subs to scope
   [pinkgorilla.events] ; bring all events to scope
   [pinkgorilla.util :refer [application-url ws-origin]]
   [pinkgorilla.views :as v]
   [pinkgorilla.editor.core :as editor]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.kernel.nrepl :as nrepl-kernel]

    ;[widget.replikativ]
   ))

#_(defn ^:before-load my-before-reload-callback []
    (info "BEFORE reload!!!"))

#_(defn ^:after-load my-after-reload-callback []
    (info "AFTER reload!!!"))

(defn mount-root
  []
  (ra/render [v/gorilla-app] (.getElementById js/document "react-app")))

(defn ^:export init! []
  (routes/app-routes)
  (editor/init-cm-globally!)
  (v/init-mathjax-globally!)
  (let [app-url (application-url)
        route (:anchor app-url)
        read-write (or (not route) (not (str/index-of route "/view")))]
    (dispatch-sync [:initialize-app-db app-url])
    ;; TODO config (+ settings-local-storage) init should kick off off after config is processed
    (dispatch-sync [:settings-localstorage-load])
    (dispatch-sync [:initialize-config])
    (mount-root)
    (when read-write
      (nrepl-kernel/init! (ws-origin "repl/" app-url)))
    (if (not route)
      (routes/nav! "/new")
      (secretary/dispatch! route))))
