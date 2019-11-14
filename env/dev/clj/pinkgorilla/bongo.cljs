
(ns  pinkgorilla.bongo
  (:require
   [devcards.core]
   [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard defcard-rg defcard-doc]]))

(defn reagent-component-example []
  [:div "hey there"
    [:div.flex-auto.sans-serif.f6.bg-white.bb.b--near-white.bw2
     [:div.sans-serif.pa2.f7.b.flex.items-center.pointer.hover-opacity-parent
       [:span "asdf"]
      
     ]]
      
      ])

(defcard reagent-component
  "lorenz ipsum"
  (reagent/as-element [reagent-component-example]))