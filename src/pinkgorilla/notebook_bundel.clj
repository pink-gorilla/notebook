(ns pinkgorilla.notebook-bundel
  (:require
   [pinkgorilla.notebook-ui.app.app :refer [notebook-run!]])
  (:gen-class))

(defn run! [{:keys [profile config]
             :or {profile "jetty"
                  config "notebook-bundel.edn"}}]
  (notebook-run!
   {:profile profile
    :config config}))

(defn -main ; for lein alias
  ([]
   (run! {}))
  ([config]
   (run! {:config config})))


