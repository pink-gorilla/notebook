(ns pinkgorilla.notebook-ui.nrepl.subscriptions
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :nrepl/connected?
 (fn [db _]
   (get-in db [:nrepl :connected?])))

(rf/reg-sub
 :nrepl/info
 (fn [db _]
   (get-in db [:nrepl :info])))

(rf/reg-sub
 :nrepl/conn
 (fn [db _]
   (let [conn-a (get-in db [:nrepl :conn :conn])]
     (when conn-a
       @conn-a))))

(rf/reg-sub
 :nrepl
 (fn [db [_]]
   (:nrepl db)))
