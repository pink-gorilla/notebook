(ns pinkgorilla.output.core
  (:require
   [pinkgorilla.output.html :refer [output-html]]
   [pinkgorilla.output.list-like :refer [output-list-like]]
   [pinkgorilla.output.jsscript :refer [output-jsscript]]
   [pinkgorilla.output.reagent :refer [output-reagent]]))

;; 2019 10 16 awb:
;; output-list-like needs output-fn as recursive-renderer.
;; since cross-namespace "declare" does not work, we do partial
;; application and pass output-fn to output-list-like

(defn output-unknown
  [output _]
  (output-html "<p> Error: Unknown output-type </p>" output))

(defn output-fn
  [value-output]
  (case (:type value-output)
    :html output-html ; todo: create html component in gorilla-renderable-ui and remove :html
    :list-like (partial output-list-like output-fn)
    :reagent output-reagent
    :jsscript output-jsscript ; todo: remove this
    output-unknown))