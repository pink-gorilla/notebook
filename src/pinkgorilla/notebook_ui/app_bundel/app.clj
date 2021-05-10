(ns pinkgorilla.notebook-ui.app-bundel.app
  (:require
   [taoensso.timbre :refer [info warn]]
   ; webly
   [webly.config :refer [load-config! get-in-config add-config]]
   [webly.profile :refer [server?]]
   [webly.user.app.app :refer [webly-run!]]
   ; notebook
   [pinkgorilla.notebook-ui.app.app :refer [run-notebook-services!]]
   ; goldly
   [goldly.app :refer [goldly-run!]]
   ; bundel
   [pinkgorilla.notebook-ui.app-bundel.routes])
  (:gen-class))

(defn run-goldly! []
  (let [goldly-enabled (get-in-config [:goldly :enabled])]
    (if goldly-enabled
      (goldly-run!)
      (warn "goldly is disabed!"))))

(defn notebook-bundel-run!
  ([]
   (notebook-bundel-run! nil "jetty"))
  ([user-config]
   (notebook-bundel-run! user-config "jetty"))
  ([user-config profile]
   (let [config (add-config "notebook-bundel.edn" user-config)]
     (if (server? profile)
       (do (load-config! config)
           (run-goldly!)
           (run-notebook-services!))
       (warn "no server mode. not running explorer/goldly/nrepl"))
     (webly-run! profile config))))

(defn -main
  ([]
   (notebook-bundel-run!))
  ([profile]
   (notebook-bundel-run! nil profile)))
