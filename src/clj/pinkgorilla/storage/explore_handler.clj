(ns pinkgorilla.storage.explore-handler
  ""
  (:require
   [clojure.tools.logging :refer (info)]
   [ring.util.response :as res]
   [pinkgorilla.system :as sys]

   [pinkgorilla.storage.files :as files]
   [pinkgorilla.explore.file :refer [explore]]))


;; More ugly atom usage to support defroutes
(def ^:private excludes (atom #{".git"}))

(defn update-excludes
  [fn]
  (swap! excludes fn))


;; API endpoint for getting the list of worksheets in the project
(defn gorilla-files
  [req]
  (let [excludes @excludes]
    (res/response {:files (files/gorilla-filepaths-in-current-directory excludes)})))


;; API endpoint for file-system exploration
;; This returns not only filenames, but full meta-data

(defn- explore-directories []
  (let [notebook-paths (sys/get-in-system [:config :config :explore-file-directories])
        _ (info "notebook-paths is: " notebook-paths)
        dirs (map explore notebook-paths)]
    (reduce concat [] dirs)))


(defn req-explore-directories
  [req]
  (res/response {:data (explore-directories)}))

