(ns pinkgorilla.explore.handlers
  (:require
   [re-frame.core :refer [reg-event-db dispatch trim-v path]]
   ;[ajax.core :refer [GET POST DELETE PUT]]
   ;[accountant.core :as a]
   ;[open-source.routes :as r]
    [pinkgorilla.routes :as r]
   [pinkgorilla.explore.utils :as u]
   ;[open-source.routes :as r]
   ;[open-source.db :as db]
   ;[open-source.handlers.common :as c]
   ))

(reg-event-db
 :list-projects
 [trim-v]
 (fn [db [tags]]
   (println ":list-projects tags: " tags)
   (-> db
       (merge {:nav {:l0 :public
                     :l1 :projects
                     :l2 :list}})
       (assoc-in [:main] :explore)
       (assoc-in [:forms :projects :search :data :tags] tags))))

;; ===========
;; search
;; ===========


(defn rnav [query-params]
  (println "nav dispatch..")
  (dispatch [:list-projects (set (u/split-tags (:tags query-params)))]))


;; filter by tag
(reg-event-db
 :toggle-tag
 [trim-v]
 (fn [db [tag]]
   (println "filtering by tag:" tag)
   (let [tags (set (get-in db [:forms :projects :search :data :tags]))
         new-tags (if (tags tag) (disj tags tag) (conj tags tag))]
     (println "path:" (r/projects-path {:query-params {:tags (clojure.string/join "," (sort new-tags))}}))
     ;(r/nav (r/projects-path {:query-params {:tags (clojure.string/join "," (sort new-tags))}}))
     (rnav  {:tags (clojure.string/join "," (sort new-tags))} )
     db)))

;; filter by text 
(reg-event-db 
 :edit-field
 [trim-v]
 (fn [db [path val]] (assoc-in db path val)))