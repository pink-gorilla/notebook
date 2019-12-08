(ns ^:figwheel-hooks pinkgorilla.core
  (:require
   [clojure.string :as str]
   [taoensso.timbre :refer-macros (info)]
   [cemerick.url :as url]
   [secretary.core :as secretary]
   [reagent.core :as ra]
   [re-frame.core :refer [dispatch-sync]]

   [pinkgorilla.prefs :as prefs]
   [pinkgorilla.events]
   [pinkgorilla.views :as v]
   [pinkgorilla.editor.core :as editor]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.kernel.nrepl :as nrepl]
   [pinkgorilla.kernel.cljs :as cljs-kernel]
   [pinkgorilla.notifications :refer [add-notification notification]]

    ;[widget.replikativ]
   ))

;; shadow-cljs does not support require outside ns as of 2.8.80!
;; https://anmonteiro.com/2016/10/clojurescript-require-outside-ns/
#_(prefs/if-cljs-kernel
   (require '[pinkgorilla.kernel.shadowcljs :as cljs-kernel])
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
  (dispatch-sync [:settings-localstorage-load])
  (dispatch-sync [:initialize-config])
  (dispatch-sync [:explore-load])
  (add-notification (notification :warning "The sky is blue. Gorillas are Pink."))
  (cljs-kernel/init!)
  (let [app-url (url/url (-> js/window .-location .-href))
        route (:anchor app-url)
        read-write (or (not route) (not (str/index-of route "/view")))]
    (dispatch-sync [:initialize-view-db app-url])
    (if read-write
      (do
        (nrepl/start-ws-repl! "repl/" app-url)
        ;(dispatch-sync [:initialize-config])
        ;(dispatch-sync [:explore-load])
        ))
    (mount-root)
    (if (not route)
      (routes/nav! "/new")
      (secretary/dispatch! route))))
