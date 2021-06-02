(ns pinkgorilla.notebook-bundel
  (:require
   [pinkgorilla.notebook-ui.app.app :refer [notebook-run!]])
  (:gen-class))

(defn run! []
  (notebook-run!
   {:profile "jetty"
    :config "notebook-bundel.edn"}))

(defn -main ; for lein alias
  []
  (run!))

