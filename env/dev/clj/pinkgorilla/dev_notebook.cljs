(ns pinkgorilla.dev-notebook
  (:require
   [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf info)]
   [re-frame.core :refer [dispatch clear-subscription-cache!]]
   [pinkgorilla.app-notebook :refer [mount-notebook-app]]
   [pinkgorilla.tenx.config :refer [configure-10x!]]))

;; this gets executed when using run-repls-with-jdsa


(enable-console-print!)

(timbre/set-level! :debug)
;(timbre/set-level! :info)

; TODO: Dont remove this println; otherwise println will not work in the 
; rest of the application. why is this???
(println "pinkgorilla shadow-reloading should be working!")

(configure-10x!)

;; see:
;; https://shadow-cljs.github.io/docs/UsersGuide.html#_lifecycle_hooks
;; https://code.thheller.com/blog/shadow-cljs/2019/08/25/hot-reload-in-clojurescript.html

;; configuration is done EITHER
;; - EITHER via METADATA
;; - OR shadow-cljs.edn :devtools section   


;; before-reload is a good place to stop application stuff before we reload.
(defn ^:dev/before-load before-reload []
  (println "shadow-cljs reload: before")
  (info "shadow-cljs reload: before"))

(defn ^:dev/after-load after-reload []
  (enable-console-print!)
  (timbre/set-level! :debug)
  (println "shadow-cljs reload: after")
  (info "shadow-cljs reload: after")

  (println "clearing reframe subscription cache..")
  (clear-subscription-cache!)

  (println "re-loading configuration from server..")
  (dispatch [:load-config])

  (println "mounting notebook-app ..")
  (mount-notebook-app))


