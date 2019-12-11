(ns pinkgorilla.storage.explore-handler
  ""
  (:require
   [clojure.tools.logging :refer (info)]
   [ring.util.response :as res]
   [pinkgorilla.system ]

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

(def config {:explore-file-directories ["./notebooks"
                                        "/home/andreas/Documents/clojure/clojureQuant/gorilla"]})

(defn- explore-directories []
  (let [;_  (info "config is:" (keys (get-in (pinkgorilla.system/gorilla-system {})[:config :runtime-config])))
;config (gorilla-system {})
        ;_ (info "config is: " config)
        notebook-paths (:explore-file-directories config)
        dirs (map explore notebook-paths)]
    (reduce concat [] dirs)))


(defn req-explore-directories
  [req]
  (res/response {:data (explore-directories)}))

