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
(ns zealous-autumn
  (:require [gorilla-plot.core :as plot]))
;; @@

;; @@
(use '[cemerick.pomegranate :only (add-dependencies)])

;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(add-dependencies :coordinates '[;[algebolic "1.0.0"]
                                 [org.clojars.deas/gorilla-renderable "2.1.0"]]
                                   
                  :repositories (merge cemerick.pomegranate.aether/maven-central
                                 {"clojars" "https://clojars.org/repo"}))
;; @@
;; =>
;;; ["^ ","~:type","list-like","~:open",["span",["^ ","~:class","clj-map"],"{"],"~:close",["span",["^ ","^2","clj-map"],"}"],"~:separator",["span",", "],"~:items",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","~:content",["span",["^ ","^2","clj-symbol"],"org.clojars.deas/gorilla-renderable"],"~:value","org.clojars.deas/gorilla-renderable"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"2.1.0\""],"^7","\"2.1.0\""]],"^7","[org.clojars.deas/gorilla-renderable \"2.1.0\"]"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-set"],"#{"],"^3",["span",["^ ","^2","clj-set"],"}"],"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/clojure"],"^7","org.clojure/clojure"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.5.1\""],"^7","\"1.5.1\""]],"^7","[org.clojure/clojure \"1.5.1\"]"]],"^7","#{[org.clojure/clojure \"1.5.1\"]}"]],"^7","[[org.clojars.deas/gorilla-renderable \"2.1.0\"] #{[org.clojure/clojure \"1.5.1\"]}]"],["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/clojure"],"^7","org.clojure/clojure"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.5.1\""],"^7","\"1.5.1\""]],"^7","[org.clojure/clojure \"1.5.1\"]"],["^ ","^0","html","^6",["span",["^ ","^2","clj-nil"],"nil"],"^7","nil"]],"^7","[[org.clojure/clojure \"1.5.1\"] nil]"]],"^7","{[org.clojars.deas/gorilla-renderable \"2.1.0\"] #{[org.clojure/clojure \"1.5.1\"]}, [org.clojure/clojure \"1.5.1\"] nil}"]
;; <=

;; @@

;; @@
