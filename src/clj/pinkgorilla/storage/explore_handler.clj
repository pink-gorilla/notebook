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
  [_]
  (let [excludes @excludes]
    (res/response {:files (files/gorilla-filepaths-in-current-directory excludes)})))

;; API endpoint for file-system exploration
;; This returns not only filenames, but full meta-data

(defn explore-dir [[name dir]]
  (info "exploring notebooks for repo " name " in " dir)
  (->> (explore dir)
       (map #(assoc % :repo name :root-dir dir))
       (vec)))

(defn- explore-directories []
  (let [notebook-paths (sys/get-setting [:explore-file-directories])
        _ (info "exploring setting: " notebook-paths)
        dirs (map explore-dir notebook-paths)
        _ (info "exploring in directories: " dirs)]
    (reduce concat [] dirs)))

(defn req-explore-directories
  [_]
  (res/response {:data (explore-directories)}))

