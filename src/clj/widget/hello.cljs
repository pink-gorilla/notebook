(ns widget.hello
  (:require
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [taoensso.timbre :refer-macros (info)]
   ))

(defn world [& [name]] ; name==state. state is an atom
   (reagent/create-class {
     :display-name "hello-world"
     :reagent-render (fn []
                       [:div
                        (if (nil? name) ; we check if we got an atom.
                          [:div
                             [:h1 "Hello"]
                             [:h2 "World, from Reagent."]]
                          [:h2 (str "It is so NICE to see " @name)]
                          )])}))

(defn love [activity]
  (reagent/create-class
   {:display-name "love"
    :reagent-render
    (fn []
      (info "activity is: " activity)
      [:h1
       (if (nil? activity)
         "I love THE WHOLE WORLD!"
         (str "I love " activity))])}))
