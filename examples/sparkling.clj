;; gorilla-repl.fileformat = 2

;; **
;;; # Gorilla REPL
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns harmonious-aurora
  (:use [cemerick.pomegranate :only (add-dependencies)])
  (:require [gorilla-plot.core :as plot]))
;; @@

;; @@
(add-dependencies :coordinates '[[gorillalabs/sparkling "1.2.5"]
                                 [org.apache.spark/spark-core_2.10 "1.5.1"]]
                  :repositories (merge cemerick.pomegranate.aether/maven-central
                                          {"clojars" "http://clojars.org/repo"}))
;; @@

;; @@
(require '[sparkling.conf :as conf])
;; @@

;; @@
(require '[sparkling.core :as spark])
;; @@

;; @@
(def c (-> (conf/spark-conf)
           (conf/master "local")
           (conf/app-name "sparkling-example")))
;; @@

;; @@
(def sc (spark/spark-context c))
;; @@
