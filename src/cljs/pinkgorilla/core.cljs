(ns ^:figwheel-hooks pinkgorilla.core
  (:require
    [clojure.string :as str]
    [reagent.core :as ra]
    [re-frame.core :refer [dispatch-sync]]
    [cemerick.url :as url]
    [secretary.core :as secretary]
    [pinkgorilla.prefs :as prefs]
    [pinkgorilla.events]
    [pinkgorilla.views :as v]
    [pinkgorilla.editor.core :as editor]
    [pinkgorilla.routes :as routes]
    [pinkgorilla.kernel.nrepl :as nrepl]
    [pinkgorilla.kernel.browser :as brwrepl]
    [taoensso.timbre :refer-macros (info)]
    ;[widget.replikativ]
    ))

(prefs/if-cljs-kernel
 (require '[pinkgorilla.kernel.klipsecljs :as cljs-kernel])
 (require '[pinkgorilla.kernel.mock :as cljs-kernel]))

(defn ^:before-load my-before-reload-callback []
  (info "BEFORE reload!!!"))

(defn ^:after-load my-after-reload-callback []
  (info "AFTER reload!!!"))

(defn mount-root
  []
  (ra/render [v/gorilla-app] (.getElementById js/document "react-app")))

(defn ^:export init! []
  ;(widget.replikativ/setup-replikativ)
  (routes/app-routes)
  (editor/init-cm-globally!)
  (v/init-mathjax-globally!)
  (cljs-kernel/init-klipse!)
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
