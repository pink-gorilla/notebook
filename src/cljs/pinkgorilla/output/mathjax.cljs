
(ns pinkgorilla.output.mathjax
  (:require
    [taoensso.timbre :as timbre
     :refer-macros (log trace debug info warn error fatal report
                        logf tracef debugf infof warnf errorf fatalf reportf
                        spy get-env log-env)]
  ))


(defn queue-mathjax-rendering
  [id]
  (if-let [mathjax (.-MathJax js/window)]
    (doto (.-Hub mathjax)
      (.Queue #js ["Typeset" (.-Hub mathjax) id]))
    (warn "Missing global MathJax")))