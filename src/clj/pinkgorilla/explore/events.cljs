(ns pinkgorilla.explore.events
  (:require
   [re-frame.core :refer [reg-event-db trim-v]]
   [pinkgorilla.routes :as r]
   ; bring to scope:
   [pinkgorilla.explore.events-load-notebooks]))

;; this does not belong here; but where?
(reg-event-db
 :renderer-show
 [trim-v]
 (fn [db]
   (println ":renderer")
   (-> db
       (assoc-in [:main] :renderer))))

(reg-event-db
 :explorer-show
 [trim-v]
 (fn [db [tags]]
   (println ":explorer-show tags: " tags)
   (-> db
       (assoc-in [:main] :explore)
       (assoc-in [:explorer :search :tags] tags))))

(reg-event-db
 :explorer-search-text
 (fn [db [_ text]]
   (assoc-in db [:explorer :search :text] text)))

(reg-event-db
 :explorer-toggle-tag
 [trim-v]
 (fn [db [tag]]
   (let [tags-set (get-in db [:explorer :search :tags])
         tags-set-new (if (tags-set tag) (disj tags-set tag) (conj tags-set tag))]
     (assoc-in db [:explorer :search :tags] tags-set-new))))




