(ns pinkgorilla.events.notebook
  (:require
   [clojure.string :as str]
   [re-frame.core :as re-frame :refer [reg-event-db]]
   [day8.re-frame.http-fx]

   [pinkgorilla.db :as db :refer [initial-db]]
   [pinkgorilla.notebook.newnb :refer [create-new-worksheet]]
   ;[pinkgorilla.explore.subs]
   ;[pinkgorilla.explore.handlers]
   ))


(defn- view-db
  [app-url]
  (let [base-path (str/replace (:path app-url) #"[^/]+$" "")
        db (merge initial-db {:base-path base-path})]
    db))


(reg-event-db
 :initialize-view-db
 (fn [_ [_ app-url]]
   (view-db app-url)))

(reg-event-db
 :initialize-new-worksheet
 (fn [db _]
   (assoc db :worksheet (create-new-worksheet))))

(reg-event-db
 :worksheet:segment-clicked
 (fn [db [_ segment-id]]
   (assoc-in db [:worksheet :active-segment] segment-id)))

(reg-event-db
 :segment-value-changed
 (fn [db [_ seg-id value]]
   (assoc-in db [:worksheet :segments seg-id :content :value] value)))
