(ns pinkgorilla.events.common
  (:require
   [re-frame.core :as rf :include-macros true]
            ;[day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(defn reg-set-attr
  "helper function to register a reframe event-db :event-name
   attr-name is a vector of keys within the app-db
   when triggered it will do assoc-in in the app-db in the right location.
   "
  [evt-name attr-name]
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
