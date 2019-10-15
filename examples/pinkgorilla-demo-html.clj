;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla Demo - html rendering
;; **

;; @@
(ns demo-html
  (:require [pinkgorilla.plugins.hickup :refer [html!]]))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(html! [:h4 "hello, world"])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content","<h4>hello, world</h4>"]
;; <=

;; @@
(html! [:ol 
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
(html! [:ol])      
             
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content","<ol></ol>"]
;; <=

;; @@
; DO NOT REMOVE THIS CONTENT - THIS WILL FUCK UP THE RENDERING AND LOADING OF THE SHEET!!
(comment 
  (map #(assoc {} :a %)  (range 10)))
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@

;; @@
