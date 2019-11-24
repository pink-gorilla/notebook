(ns pinkgorilla.output.core
  (:require
   ;[pinkgorilla.output.vega :refer [output-vega]]
   [pinkgorilla.output.html :refer [output-html]]
   [pinkgorilla.output.latex :refer [output-latex]]
   [pinkgorilla.output.list-like :refer [output-list-like]]
   
   [pinkgorilla.output.widget :refer [output-widget]]
   [pinkgorilla.output.jsscript :refer [output-jsscript]]
   [pinkgorilla.output.reagent :refer [output-reagent]]
   [pinkgorilla.output.reagent-cljs :refer [output-reagent-cljs]]
   ))

;; 2019 10 16 awb:
;; output-list-like needs output-fn as recursive-renderer.
;; since cross-namespace "declare" does not work, we do partial
;; application and pass output-fn to output-list-like

(defn output-unknown
  [output _]
  (output-html "<p> Error: Unknown output-type </p>" nil))

(defn output-fn
  [value-output]
  (case (:type value-output)
    ; awb99 a hack - cljs renderer gives back keyword. todo: clj repl has to give back keyword also
    :html output-html
    :list-like (partial output-list-like output-fn)
    ;:vega output-vega
    :latex output-latex
    :widget output-widget
    :reagent output-reagent
    :reagent-cljs output-reagent-cljs
    :jsscript output-jsscript
    
    "html" output-html
    "list-like" (partial output-list-like output-fn)
    ;"vega" output-vega
    "latex" output-latex
    "widget" output-widget
    "reagent" output-reagent
    "jsscript" output-jsscript
    output-unknown
    ))