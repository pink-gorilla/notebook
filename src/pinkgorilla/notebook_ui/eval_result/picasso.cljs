(ns pinkgorilla.notebook-ui.eval-result.picasso
  (:require
   [picasso.protocols :refer [paint]]
   [picasso.paint.hiccup :refer [->reagent]]))

(defn picasso-cell-raw [picasso-spec]
  (let [r (if (map? picasso-spec)
            [:div.flex-grow-1.result-one (paint picasso-spec)] ; one spec
            (into [:div.flex-grow-1.flex.flex-col.result-many] ; .mt-5 .w-full.h-full.prose
                  (map (fn [s] (paint s)) picasso-spec))) ; multiple specs
        ; r (->reagent picasso-spec)
        ]
    r))

#_(defn picasso-cell [picasso-spec]
    [:div
     [:p.bg-blue-500 "picasso-spec:" (pr-str picasso-spec)]
     [:p.bg-blue-500 "picasso-spec second:" (pr-str (second picasso-spec))]
     [picasso-cell-raw picasso-spec]])

(def picasso-cell picasso-cell-raw)
