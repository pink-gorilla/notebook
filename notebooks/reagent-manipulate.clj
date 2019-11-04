;; gorilla-repl.fileformat = 2

;; **
;;; # Reagent Widgets with Dynamic State
;; **

;; @@
; Define Namespace for your notebook and require namespaces 
(ns pacific-pond  
  (:require 
     [pinkgorilla.ui.widget :refer [widget!]] 
     [pinkgorilla.ui.hiccup :refer [html!]] 
     [pinkgorilla.ui.reagent :refer [reagent!]]))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; **
;;; vega charts rendered with **reagent**
;; **

;; @@
(reagent!
 '[:div
   [:p "sample vega plot:"]
  [vega "https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json"]])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","reagent","~:content",["div",["p","sample vega plot:"],["vega","https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json"]],"~:value","[:div [:p \"sample vega plot:\"] [vega \"https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json\"]]"]
;; <=

;; **
;;; manipulate {:samples [:name "bar-chart" :value "https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json"] }
;;;  [:p "sample"]
;;;  
;; **

;; @@
(reagent!
 '[:div
   [:p "sample vega plot:"]
   [combo :widget-state :x 
     [{:label "bar-chart" :id "https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json"}
      {:label "population-pyramid" :id "https://vega.github.io/vega/examples/population-pyramid.vg.json"}
      {:label "tree" :id "https://vega.github.io/editor/spec/vega/tree-layout.vg.json"}]]
   [:p "And the chart:"]
   [vegaa :widget-state :x]
 
   ])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","reagent","~:content",["div",["p","sample vega plot:"],["combo","widget-state","x",[["^ ","~:label","bar-chart","~:id","https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json"],["^ ","^2","population-pyramid","^3","https://vega.github.io/vega/examples/population-pyramid.vg.json"]]],["p","And the chart:"],["vegaa","widget-state","x"]],"~:value","[:div [:p \"sample vega plot:\"] [combo :widget-state :x [{:label \"bar-chart\", :id \"https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json\"} {:label \"population-pyramid\", :id \"https://vega.github.io/vega/examples/population-pyramid.vg.json\"}]] [:p \"And the chart:\"] [vegaa :widget-state :x]]"]
;; <=

;; @@
(reagent!
 '[:div
   [vegaa :widget-state :x]
   ])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","reagent","~:content",["div",["vegaa","widget-state","x"]],"~:value","[:div [vegaa :widget-state :x]]"]
;; <=

;; @@

;; @@
;; ->
;;; 
;; <-

;; @@

;; @@
;; ->
;;; 
;; <-
