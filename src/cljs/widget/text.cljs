(ns widget.text
  (:require
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [taoensso.timbre :refer-macros (info)]
   ))


(defn update-key [a k v]
  (info "updating atom key:" k " to val:" v)
  (swap! a assoc k v))

(defn atom-text
  "textbox that is bound to a key of an external atom"
  [a k]
  [:input {:type "text"
           :value (if (nil? (k @a)) "" (k @a))
           :on-change #(update-key a k (-> % .-target .-value))}])

