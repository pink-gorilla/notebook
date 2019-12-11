
(ns pinkgorilla.events.common
  (:require [re-frame.core :as rf :include-macros true]
            ;[day8.re-frame.tracing :refer-macros [fn-traced]]
            [reagent.core :as reagent :refer [atom]]))

(defn reg-set-attr [evt-name attr-name]
  (cond
    (keyword? attr-name)
    (rf/reg-event-db
     evt-name
     (fn ; fn-traced
      [db [_ obj]]
      (assoc db attr-name obj)))
    (vector? attr-name)
    (rf/reg-event-db
     evt-name
     (fn ; fn-traced
      [db [_ obj]]
      (-> db
          (assoc-in attr-name obj))))))