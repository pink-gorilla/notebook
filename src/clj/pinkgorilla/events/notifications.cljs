(ns pinkgorilla.events.notifications
  (:require [re-frame.core :as rf :include-macros true]
            ;[day8.re-frame.tracing :refer-macros [fn-traced]]
            ))

;; stolen from:
;; https://github.com/baskeboler/cljs-karaoke-client/blob/master/src/main/cljs_karaoke/events/notifications.cljs

(rf/reg-event-fx
 ::add-notification
 (fn ; fn-traced
  [{:keys [db]} [_ n]]
  {:db (-> db (update :notifications conj n))
   :dispatch-later [{:ms 5000
                     :dispatch [::dismiss-notification (:id n)]}]}))

(rf/reg-event-db
 ::dismiss-notification
 (fn ; fn-traced
  [db [_ notification-id]]
  (-> db
      (update :notifications (fn [notis]
                               (filterv
                                #(not= notification-id (:id %))
                                notis))))))