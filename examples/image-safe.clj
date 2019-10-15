;; gorilla-repl.fileformat = 2

;; **
;;; # Image-Safe
;;; 
;;; 
;;; Bugs:
;;; 1. dependency loading has to be commented out, because leaving it uncommented no longer loads the sheet.
;;; 2. The Markdown "load-gorilla dependency" cannot be edited
;; **

;; @@
(ns charming-chasm
  (:require [gorilla-plot.core :as plot]))
;; @@
;; ->
;;; 
;; <-

;; **
;;; Load gorialla-renderable dependency
;; **

;; @@
(comment
  (use '[cemerick.pomegranate :only (add-dependencies)])
  (add-dependencies :coordinates '[]
                                 [org.clojars.deas/gorilla-renderable "2.1.0"]
                  :repositories (merge cemerick.pomegranate.aether/maven-central
                                 {"clojars" "https://clojars.org/repo"}))
                  
  (use 'gorilla-renderable.core))

                  

;; @@
;; =>
;;; ["^ ","~:type","list-like","~:open",["span",["^ ","~:class","clj-map"],"{"],"~:close",["span",["^ ","^2","clj-map"],"}"],"~:separator",["span",", "],"~:items",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","~:content",["span",["^ ","^2","clj-symbol"],"org.clojars.deas/gorilla-renderable"],"~:value","org.clojars.deas/gorilla-renderable"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"2.1.0\""],"^7","\"2.1.0\""]],"^7","[org.clojars.deas/gorilla-renderable \"2.1.0\"]"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-set"],"#{"],"^3",["span",["^ ","^2","clj-set"],"}"],"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/clojure"],"^7","org.clojure/clojure"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.5.1\""],"^7","\"1.5.1\""]],"^7","[org.clojure/clojure \"1.5.1\"]"]],"^7","#{[org.clojure/clojure \"1.5.1\"]}"]],"^7","[[org.clojars.deas/gorilla-renderable \"2.1.0\"] #{[org.clojure/clojure \"1.5.1\"]}]"],["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/clojure"],"^7","org.clojure/clojure"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.5.1\""],"^7","\"1.5.1\""]],"^7","[org.clojure/clojure \"1.5.1\"]"],["^ ","^0","html","^6",["span",["^ ","^2","clj-nil"],"nil"],"^7","nil"]],"^7","[[org.clojure/clojure \"1.5.1\"] nil]"]],"^7","{[org.clojars.deas/gorilla-renderable \"2.1.0\"] #{[org.clojure/clojure \"1.5.1\"]}, [org.clojure/clojure \"1.5.1\"] nil}"]
;; <=

;; @@
(defrecord Image [url])
(defn image 
  [url]
  [:img {:src url :height 100 :width 100 :alt "it's an image"}])
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-var"],"#'user/image"],"~:value","#'user/image"]
;; <=

;; @@
(comment
  (extend-type Image)
  Renderable
  (render [self]
          {:type :html
           :content (image (:url self))
           :value (pr-str self)}))
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(Image. "https://cdn.mos.cms.futurecdn.net/yyvvM9kwaVGySFE6aXXoJL-320-80.jpg")
(Image. "https://pbs.twimg.com/profile_images/641353910561566720/VSxsyxs7.jpg")
;; @@
;; =>
;;; ["^ ","~:type","list-like","~:open",["span",["^ ","~:class","clj-record"],"#user.Image{"],"~:close",["span",["^ ","^2","clj-record"],"}"],"~:separator",["span"," "],"~:items",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","~:content",["span",["^ ","^2","clj-keyword"],":url"],"~:value",":url"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"https://pbs.twimg.com/profile_images/641353910561566720/VSxsyxs7.jpg\""],"^7","\"https://pbs.twimg.com/profile_images/641353910561566720/VSxsyxs7.jpg\""]],"^7","[:url \"https://pbs.twimg.com/profile_images/641353910561566720/VSxsyxs7.jpg\"]"]],"^7","#user.Image{:url \"https://pbs.twimg.com/profile_images/641353910561566720/VSxsyxs7.jpg\"}"]
;; <=

;; @@

;; @@
