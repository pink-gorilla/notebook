(ns gorilla-repl.output.html
  (:require
    [gorilla-repl.output.hack :refer [temp-comp-hack]]))


;; TODO Ugh, old stylesheets persist as html so we get a string
(defn output-html
  [output _]
  (if-let [content (:content output)]
    (cond
      (string? content)
      [:span.value {:data-value              (:value output)
                    :dangerouslySetInnerHTML {:__html content}}]
      :else
      [:span.value {:data-value (:value output)} (temp-comp-hack (:content output))])))