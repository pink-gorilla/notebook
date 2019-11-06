(ns pinkgorilla.kernel.cljs-tools
  (:require 
    [pinkgorilla.ui.gorilla-renderable :refer [Renderable render]]))


(defn r! [vec_or_reagent_f]
  "renders a (hydrated) reagent component"
  (reify Renderable
    (render [_] 
      {:type :reagent-cljs
       :content {} ; reagent components cannot get persisted - they are living functions compiled in the notebook
       :reagent vec_or_reagent_f
             ;:value result
       })))


