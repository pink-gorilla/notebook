(ns pinkgorilla.notebook-bundel
  (:require
   [webly.config :refer [add-config]]
   [pinkgorilla.notebook-ui.app.app :refer [notebook-run!]])
  (:gen-class))

(defn run [{:keys [profile config] ; profile is used by ci to switch between npm-install, release and watch
            :or {profile "jetty"
                 config {}}}]
  (let [config (add-config "notebook-bundel.edn" config)]
    (notebook-run!
     {:profile profile
      :config config})))

(defn -main ; for lein alias
  ([]
   (run {}))
  ([config]
   (run {:config config})))




