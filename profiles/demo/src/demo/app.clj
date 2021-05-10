(ns demo.app
  (:require
   [webly.config :refer [load-config!]]
   [webly.profile :refer [server?]]
   [webly.user.app.app :refer [webly-run!]]
   [pinkgorilla.notebook-ui.app.app :refer [run-notebook-services!]]
   [demo.routes]))

(defn -main
  [profile]
  (let [config "notebook-demo.edn"]
    (when (server? profile)
      (load-config! config)
      (run-notebook-services!))
    (webly-run! profile config)))