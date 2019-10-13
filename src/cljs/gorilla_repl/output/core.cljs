(ns gorilla-repl.output.core
  (:require
   [gorilla-repl.output.vega :refer [output-vega]]
   [gorilla-repl.output.html :refer [output-html]]
   [gorilla-repl.output.latex :refer [output-latex]]
   [gorilla-repl.output.list-like :refer [output-list-like]]
   ))

(defn output-fn
  [value-output]
  (case (:type value-output)
    "html" output-html
    "list-like" output-list-like
    "vega" output-vega
    "latex" output-latex))