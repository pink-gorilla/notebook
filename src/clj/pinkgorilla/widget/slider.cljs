(ns pinkgorilla.widget.slider
  (:require
   [reagent.core :as reagent]
   [re-com.misc :as rm]
   ;[taoensso.timbre :refer-macros (info)]
   [pinkgorilla.ui.pinkie :refer [register-tag]]))

(defn info [str]
  (.log js/console str))

(defn update-key [a k v]
  (info (str "updating atom key:" k " to val:" v))
  (swap! a assoc k v))

(defn slider
  [a k min max step]
  (let [v (k @a)
        v (if (nil? v) min v)
        s (reagent/atom v)
        change (fn [v] (reset! s v)
                 (update-key a k v))]
    (fn []
      [rm/slider
       :model     s
       :min       min
       :max       max
       :step      step
       :on-change change])))

(register-tag :p/slider slider)