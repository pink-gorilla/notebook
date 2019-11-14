(ns pinkgorilla.storage.explore-handler
  ""
  (:require
   [clojure.tools.logging :refer (info)]
   [ring.util.response :as res]
   [pinkgorilla.storage.files :as files]))


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