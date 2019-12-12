(ns pinkgorilla.dialog.notifications
  (:require
   [re-frame.core :as rf :include-macros true]
   [pinkgorilla.subs]
   [pinkgorilla.notifications :refer [notification-types]]
   ))

;; stolen from:
;; https://github.com/baskeboler/cljs-karaoke-client/blob/master/src/main/cljs_karaoke/notifications.cljs


(defn type-css-class [notification-type]
  (assert (notification-types notification-type))
  (str "is-" (symbol notification-type)))


(defn notification-component [n]
  [:div.notification
   {:key (str "notification-" (:id n))
    :class (type-css-class (:type n))}
   [:button.delete
    {:on-click #(rf/dispatch [:notification-dismiss (:id n)])}]
   (:text n)])


(defn ^export notifications-container-component []
  (let [nots-subs (rf/subscribe [:notifications])]
    [:div.notifications-container
     (when (not-empty @nots-subs)
       (doall
        (for [n @nots-subs]
          ^{:key (str "notify-" (:id n))}
          [notification-component n])))]))