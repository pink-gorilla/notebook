(ns gorilla-repl.core
  (:require [gorilla-repl.events]
            [gorilla-repl.views :as v]
            [reagent.core :as ra]
            [secretary.core :as secretary]
            [cemerick.url :as url]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [gorilla-repl.editor :as editor]
            [gorilla-repl.routes :as routes]
            [gorilla-repl.nrepl-kernel :as nrepl]
            [gorilla-repl.browser-kernel :as brwrepl]
            [clojure.string :as str]))

(defn mount-root
  []
  (ra/render [v/gorilla-app] (.getElementById js/document "react-app")))

(defn ^:export init! []
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
