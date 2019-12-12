(ns pinkgorilla.output.latex
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent]
   [pinkgorilla.output.mathjax :refer [queue-mathjax-rendering]]
   [pinkgorilla.output.hack :refer [value-wrap]]))

(defn output-latex
  [output _] ; seg-id
  (let [uuid (uuid/uuid-string (uuid/make-random-uuid))
        span-kw (keyword (str "span#" uuid))]
    (reagent/create-class
     {:component-did-mount  (fn [_]
                              (queue-mathjax-rendering uuid))
      ;; :component-did-update (fn [_ _])
      :reagent-render       (fn []
                              [value-wrap
                               (get output :value)
                               [span-kw {:class                   "latex-span"
                                         :dangerouslySetInnerHTML {:__html (str "@@" (:content output) "@@")}}]])})))