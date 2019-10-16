;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla Demo - html rendering
;; **

;; @@
(ns demo-html
  (:require [pinkgorilla.ui.hickup :refer [html!]]))
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(html! [:div
         [:h4 "hello"] 
         [:div {:style "color:green;font-weight:bold"} "World!"]])
         
;; @@
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
;; =>
;;; ["^ ","~:type","html","~:content","<ol><li>The Pinkie</li><li>The Pinkie and the Brain</li><li>brain brain brain</li><li>What are we doing today?</li></ol>"]
;; <=

;; @@
; DO NOT REMOVE THIS CONTENT - THIS WILL FUCK UP THE RENDERING AND LOADING OF THE SHEET!!
(comment 
  (map #(assoc {} :a %)  (range 10)))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@

;; @@
;; ->
;;; 
;; <-
