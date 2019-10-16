(ns gorilla-repl.output.core
  (:require
   [gorilla-repl.output.vega :refer [output-vega]]
   [gorilla-repl.output.html :refer [output-html]]
   [gorilla-repl.output.latex :refer [output-latex]]
   [gorilla-repl.output.list-like :refer [output-list-like]]
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
    "vega" output-vega
    "latex" output-latex))