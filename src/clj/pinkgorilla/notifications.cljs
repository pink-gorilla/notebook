(ns pinkgorilla.notifications
  (:require 
   [re-frame.core :as rf :include-macros true]
  ))

;; stolen from:
;; https://github.com/baskeboler/cljs-karaoke-client/blob/master/src/main/cljs_karaoke/notifications.cljs


(def notification-types #{:info :warning :danger :primary :success})

(defn notification
  ([type text]
   (assert (notification-types type))
   {:id (random-uuid)
    :type type
    :text text})
  ([text]
   (notification :primary text)))


(defn ^export add-notification [n]
  (cond
    (map? n) (rf/dispatch [:notification-add n])
    (string? n) (rf/dispatch [:notification-add (notification n)])))
