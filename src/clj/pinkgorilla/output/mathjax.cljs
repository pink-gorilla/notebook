(ns pinkgorilla.output.mathjax
  (:require
   [taoensso.timbre :refer-macros (warn)]))

(defn queue-mathjax-rendering
  [id]
  (if-let [mathjax (.-MathJax js/window)]
    (doto (.-Hub mathjax)
      (.Queue #js ["Typeset" (.-Hub mathjax) id]))
    (warn "Missing global MathJax")))
