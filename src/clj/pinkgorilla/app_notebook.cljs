(ns pinkgorilla.app-notebook
  (:require
   [clojure.string :as str]
   [secretary.core :as secretary]
   [reagent.dom]
   [re-frame.core :refer [dispatch-sync]]
  ;; PinkGorilla Notebook
   [pinkgorilla.config.notebook] ; bring pinkie renderers into scope
  ;; [pinkgorilla.events.usage-analytics]
   [pinkgorilla.subs] ; bring subs to scope
   [pinkgorilla.events] ; bring all events to scope
   [pinkgorilla.util :refer [application-url ws-origin]]
   [pinkgorilla.views :as v]
   [pinkgorilla.codemirror.core :as editor]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.kernel.nrepl :as nrepl-kernel]
   ;[pinkgorilla.10x.config :refer [configure-10x!]]
   ))

(defn mount-notebook-app []
  (reagent.dom/render [v/gorilla-app] (.getElementById js/document "gorilla-notebook-app")))

(defn ^:export init-notebook! []
 ; (configure-10x!) 
  (routes/app-routes)
  (editor/configure-cm-globally!)
  (let [app-url (application-url)
        route (:anchor app-url)
        read-write (or (not route) (not (str/index-of route "/view")))]
    (dispatch-sync [:initialize-app-db app-url])
    ;; TODO config (+ settings-local-storage) init should kick off off after config is processed
    (dispatch-sync [:settings-localstorage-load])
    (dispatch-sync [:load-config])
    (mount-notebook-app)
    (when read-write
      (nrepl-kernel/start-repl! (ws-origin "repl/" app-url)))
    (if (not route)
      (routes/nav! "/new")
      (secretary/dispatch! route))))
