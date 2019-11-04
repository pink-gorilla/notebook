
(ns pinkgorilla.ui.slider
  (:require
   [reagent.core :as reagent :refer [atom]]
   [re-com.misc]
   ))


(defn update-key [a k v]
  (println "updating atom key:" k " to val:" v)
  (swap! a assoc k v))

(defn slider
  [a k min max step]
  (let [v (k @a)
        v (if (nil? v) min v)
        s (reagent/atom v)
        change (fn [v] (do 
           (reset! s v)
           (update-key a k v)))]
    (fn []
        [re-com.misc/slider
          :model     s
          :min       min
          :max       max
          :step      step
          :on-change change])))
