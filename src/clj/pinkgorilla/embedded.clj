(ns pinkgorilla.embedded
  "entrypoint for notebook used as a library"
  (:require
   [clojure.core.async :refer [thread]]
   [pinkgorilla.notebook-app.cli :refer [parse-opts]]
   [pinkgorilla.notebook-app.core :refer [run-gorilla-server]]))

(defn run-notebook [config-file]
  (let [args2 ["-c" config-file]
        {:keys [options]} (parse-opts args2)]
    (run-gorilla-server options)
    nil))

(defn start-notebook [config-file]
  (thread
    (run-notebook config-file)))



