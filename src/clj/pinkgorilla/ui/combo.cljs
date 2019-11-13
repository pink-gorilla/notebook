
(ns pinkgorilla.ui.combo
  (:require
   [reagent.core :as reagent]
   [re-com.core     :refer [single-dropdown label]]
   [re-com.dropdown :refer [filter-choices-by-keyword single-dropdown-args-desc]]
   [taoensso.timbre :refer-macros (info)]
   ))


(defn update-key [a k v]
  (info "updating atom key:" k " to val:" v)
  (swap! a assoc k v))

(defn combo
  [a k choices]
  (let [selected-a (reagent/atom (k a))
        change (fn [id] (do
                            (update-key a k id)
                            (reset! selected-a id)))
        ]
    (fn []
        [single-dropdown
          :choices     choices
          :model       selected-a
          :title?      true
          :placeholder "Choose something"
          :width       "300px"
          :max-height  "400px"
          :filter-box? false
          :on-change   change])))
