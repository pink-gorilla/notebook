(ns pinkgorilla.explore.subs
  (:require
   [re-frame.core :refer [reg-sub-raw subscribe]]
   [clojure.string :as str]
   [clojure.set :as set]
   [pinkgorilla.explore.utils :as u])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn path-reaction
  [db path]
  (reaction (get-in @db path)))

(reg-sub-raw
 :key
 (fn [db [_ & path]]
   (reaction (get-in @db path))))

(reg-sub-raw
 :form-data
 (fn [db [_ & path]]
   (reaction (get-in @db (concat [:forms] path [:data])))))

(defn filter-items
  [query items]
  (if (empty? query)
    items
    (let [query (str/lower-case query)]
      (filter (fn [item]
                (not= -1
                      (.indexOf (->> (vals item)
                                     (filter string?)
                                     (str/join " ")
                                     (str/lower-case))
                                query)))
              items))))

(defn filter-query
  [db query-path items]
  (let [query (path-reaction db query-path)]
    (reaction (filter-items @query @items))))

(defn filter-tags
  [db tag-path items]
  (let [tags (path-reaction db tag-path)]
    (reaction
     (let [tag-set (set @tags)
           items @items
           _ (println "filtering tags: " tag-set " empty:" (empty? tag-set) " count:" (count tag-set) " tags not set: " @tags)]
       (if (empty? tag-set)
         items
         (filter #(every? (set (u/split-tags (get-in % [:meta :tags]))) tag-set)
                 items))))))

(defn filter-toggle
  [db toggle-path items]
  (let [toggle (path-reaction db toggle-path)]
    (reaction (if @toggle (filter (last toggle-path) @items) @items))))

(reg-sub-raw
 :filtered-projects
 (fn [db _]
   (let [items (path-reaction db [:data :projects])
         _ (println "filtered notebook item count: " (count @items))]
     (->> items
          (filter-query  db [:forms :projects :search :data :query])
          (filter-tags   db [:forms :projects :search :data :tags])
          ;(filter-toggle db [:forms :projects :search :data :project/beginner-friendly])
          ))))

(reg-sub-raw
 :project-tags
 (fn [db _]
   (let [listings (subscribe [:filtered-projects])]
     (reaction
      (->> @listings
           (mapcat (fn [l] (str/split (get-in l [:meta :tags]) ",")))
           (map (comp str/lower-case str/trim))
           distinct
           sort)))))