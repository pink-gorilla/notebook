(ns pinkgorilla.output.core
  (:require
   ;[pinkgorilla.output.vega :refer [output-vega]]
   [pinkgorilla.output.html :refer [output-html]]
   [pinkgorilla.output.latex :refer [output-latex]]
   [pinkgorilla.output.list-like :refer [output-list-like]]
   
   [pinkgorilla.output.widget :refer [output-widget]]
   [pinkgorilla.output.jsscript :refer [output-jsscript]]
   [pinkgorilla.output.reagent :refer [output-reagent]]
   ))

;; 2019 10 16 awb:
;; output-list-like needs output-fn as recursive-renderer.
;; since cross-namespace "declare" does not work, we do partial
;; application and pass output-fn to output-list-like

(defn output-fn
  [value-output]
  (case (:type value-output)
    "html" output-html
    "list-like" (partial output-list-like output-fn)
    ;"vega" output-vega
    "latex" output-latex
    
    "widget" output-widget
    "reagent" output-reagent
    "jsscript" output-jsscript
    ))