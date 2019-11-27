(ns cljs-karaoke.notifications
  (:require [re-frame.core :as rf :include-macros true]
            [cljs-karaoke.events.notifications :as events]
            [cljs-karaoke.subs :as s]))
(def notification-types #{:info :warning :danger :primary :success})

(defn type-css-class [notification-type]
  (assert (notification-types notification-type))
  (str "is-" (symbol notification-type)))

(defn notification
  ([type text]
   (assert (notification-types type))
   {:id (random-uuid)
    :type type
    :text text})
  ([text]
   (notification :primary text)))

(defn notification-component [n]
  [:div.notification
   {:key (str "notification-" (:id n))
    :class (type-css-class (:type n))}
   [:button.delete
    {:on-click #(rf/dispatch [::events/dismiss-notification (:id n)])}]
   (:text n)])

(defn ^export add-notification [n]
  (cond
    (map? n) (rf/dispatch [::events/add-notification n])
    (string? n) (rf/dispatch [::events/add-notification (notification n)])))


(defn ^export notifications-container-component []
  (let [nots-subs (rf/subscribe [::s/notifications])]
    [:div.notifications-container
     (when (not-empty @nots-subs)
       (doall
        (for [n @nots-subs]
          ^{:key (str "notify-" (:id n))}
          [notification-component n])))]))