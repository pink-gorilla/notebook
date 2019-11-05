(ns ^:figwheel-hooks pinkgorilla.core
  (:require
   [clojure.string :as str]
   [reagent.core :as ra]
   [re-frame.core :refer [dispatch dispatch-sync]]
   [cemerick.url :as url]
   [secretary.core :as secretary]

   [pinkgorilla.events]
   [pinkgorilla.views :as v]
   [pinkgorilla.editor.core :as editor]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.kernel.nrepl :as nrepl]
   [pinkgorilla.kernel.browser :as brwrepl]

   ;[widget.replikativ]
   ))

(defn ^:before-load my-before-reload-callback []
  (println "BEFORE reload!!!"))

(defn ^:after-load my-after-reload-callback []
  (println "AFTER reload!!!"))

(defn mount-root
  []
  (ra/render [v/gorilla-app] (.getElementById js/document "react-app")))

(defn ^:export init! []
  ;(widget.replikativ/setup-replikativ)
  (routes/app-routes)
  (editor/init-cm-globally!)
  (v/init-mathjax-globally!)
  (let [app-url (url/url (-> js/window .-location .-href))
        route (:anchor app-url)
        read-write (or (not route) (not (str/index-of route "/view")))]
    (dispatch-sync [:initialize-view-db app-url])
    (if read-write
      (do
        (nrepl/start-ws-repl! "repl" app-url)
        (dispatch-sync [:initialize-config])))
    (mount-root)
    (if (not route)
      (routes/nav! "/new")
      (secretary/dispatch! route))))
