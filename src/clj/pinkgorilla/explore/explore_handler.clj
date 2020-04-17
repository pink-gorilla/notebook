(ns pinkgorilla.explore.explore-handler
  ""
  (:require
   [clojure.tools.logging :refer (info)]
   [ring.util.response :as res]
   ;PinkGorilla Libraries
   [pinkgorilla.explore.file :refer [gorilla-filepaths-in-current-directory]]
   [pinkgorilla.explore.explorer-service]
   ;PinkGorilla Notebook
   [pinkgorilla.notebook-app.system :as sys]))


;; ugly atom usage to support defroutes


(def ^:private excludes (atom #{".git"}))

(defn update-excludes
  [fn]
  (swap! excludes fn))


;; API endpoint for getting the list of worksheets in the project
;; This is used by the legacy file explorer


(defn gorilla-files
  [_]
  (let [excludes @excludes]
    (res/response {:files (gorilla-filepaths-in-current-directory excludes)})))

;; API endpoint for file-system exploration
;; This returns not only filenames, but full meta-data

(defn req-explore-directories-sync
  [_]
  (res/response {:data (pinkgorilla.explore.explorer-service/explore-directories)}))

;; Async

(defn explore-directories-start []
  (let [notebook-paths (sys/get-setting [:explore-file-directories])
        _ (info "exploring setting: " notebook-paths)]
    (pinkgorilla.explore.explorer-service/start @excludes notebook-paths)))

(defn req-explore-directories-async
  [_]
  (res/response {:data (pinkgorilla.explore.explorer-service/notebooks)}))
