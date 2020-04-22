(ns pinkgorilla.notebook.repl
  "important stuff that will be needed by notebook users.
   it should be easy to find this functions, so they in one namespace."
  (:require
   [cemerick.pomegranate :as pg]
   [clojure.java.io :as io])
  (:import (java.io PushbackReader)))

; shamelessly stolen from: https://github.com/clojupyter/clojupyter/blob/40c6d47ec9c9e4634c8e28fca3209b5c3ac8430c/src/clojupyter/misc/helper.clj

; this code will have to stay in the core pink-gorilla repo.
; but dependency management can be moved to add-lib
; add-dependencies   

(defn add-dependencies
  "Use Pomegranate to add dependencies 
   with Maven Central and Clojars as default repositories.
   Same Syntax as clojupyter"
  [dependencies & {:keys [repositories]
                   :or {repositories {"central" "https://repo1.maven.org/maven2/"
                                      "clojars" "https://clojars.org/repo"}}}]
  (let [first-item (first dependencies)]
    (if (vector? first-item)
      ; [ [dep1] [dep2]]
      (pg/add-dependencies :coordinates `~dependencies
                           :repositories repositories)
      ; [dep1]
      (pg/add-dependencies :coordinates `[~dependencies]
                           :repositories repositories))))

(defn load-edn- [resource]
  (when resource
    (try
      (-> resource
          (io/reader)
          (PushbackReader.)
          (read))
      (catch Exception _
        (throw (Exception. (str "Error parsing edn resource " resource)))))))

(defn load-edn-resource
  "libraries can dynamically add resources.
   to easily load a resource from a library, we add this as a helper function"
  [name]
  (let [;resource (io/file name)
        resource (io/resource name)]
    (if resource
      (load-edn- resource)
      (println "resource not found: " name))))

(defn info []
  {:java (-> (System/getProperties) (get "java.version"))
   :clojure (clojure-version)})

(defonce secrets-atom (atom {}))

(defn secrets []
  @secrets-atom)

(defn secret [k]
  (k @secrets-atom))

#_(defn secret
    "loads a secret (for example an api token or ssh key)
   currently expects ./test/creds.edn file

   In future will allow to access secrets that are in web browser 
   so that user can set his credentials securely.

   Note that this is a file, not a resource, as the user has to set it."
    [k]
    (let [resource (io/file "./test/creds.edn")
        ;resource (load-edn- (io/resource "creds.edn") 
          ]
      (if resource
        (k (load-edn- resource))
        (println "secret has not found creds.edn"))))




