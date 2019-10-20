;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla Demo - html rendering
;; **

;; @@
(ns demo-html
  (:require [pinkgorilla.ui.hickup :refer [html!]]))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(html! [:div
         [:h4 "hello"] 
         [:div {:style "color:green;font-weight:bold"} "World!"]])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content","<div><h4>hello</h4><div style=\"color:green;font-weight:bold\">World!</div></div>"]
;; <=

;; @@
(html! 
  [:ol 
    [:li "The Pinkie"]
    [:li "The Pinkie and the Brain"]
    [:li "brain brain brain"]
    [:li "What are we doing today?"]])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content","<ol><li>The Pinkie</li><li>The Pinkie and the Brain</li><li>brain brain brain</li><li>What are we doing today?</li></ol>"]
;; <=

;; @@
 (map #(assoc {} :a %)  (range 10))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","list-like","~:open",["span",["^ ","~:class","clj-lazy-seq"],"("],"~:close",["span",["^ ","^2","clj-lazy-seq"],")"],"~:separator",["span"," "],"~:items",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","~:content",["span",["^ ","^2","clj-keyword"],":a"],"~:value",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"0"],"^7","0"]],"^7","[:a 0]"]],"^7","{:a 0}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"1"],"^7","1"]],"^7","[:a 1]"]],"^7","{:a 1}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"2"],"^7","2"]],"^7","[:a 2]"]],"^7","{:a 2}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"3"],"^7","3"]],"^7","[:a 3]"]],"^7","{:a 3}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"4"],"^7","4"]],"^7","[:a 4]"]],"^7","{:a 4}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"5"],"^7","5"]],"^7","[:a 5]"]],"^7","{:a 5}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"6"],"^7","6"]],"^7","[:a 6]"]],"^7","{:a 6}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"7"],"^7","7"]],"^7","[:a 7]"]],"^7","{:a 7}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"8"],"^7","8"]],"^7","[:a 8]"]],"^7","{:a 8}"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-map"],"{"],"^3",["span",["^ ","^2","clj-map"],"}"],"^4",["span",", "],"^5",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-keyword"],":a"],"^7",":a"],["^ ","^0","html","^6",["span",["^ ","^2","clj-long"],"9"],"^7","9"]],"^7","[:a 9]"]],"^7","{:a 9}"]],"^7","({:a 0} {:a 1} {:a 2} {:a 3} {:a 4} {:a 5} {:a 6} {:a 7} {:a 8} {:a 9})"]
;; <=

;; @@
;; NOT WORKING: scripts can not work inside hickup (this is prevented by react)
(html! 
  [:div
    [:p "go to your webdeveloper tools and check for network trafic; highcharts should load but it does not"]
    (hiccup.page/include-js "https://code.highcharts.com/highcharts.js")])
          

;; @@
;; =>
;;; ["^ ","~:type","html","~:content","<div><p>go to your webdeveloper tools and check for network trafic; highcharts should load but it does not</p><script src=\"https://code.highcharts.com/highcharts.js\" type=\"text/javascript\"></script></div>"]
;; <=

;; @@

;; @@
;; ->
;;; 
;; <-
