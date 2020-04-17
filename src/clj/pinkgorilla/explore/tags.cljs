(ns pinkgorilla.explore.tags
  (:require
   [clojure.string :refer [lower-case trim split]]
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.components.error :refer [error-boundary]]))

(defn tags-csv->list
  [tags-csv]
  (if (or (nil? tags-csv) (= "" tags-csv))
    []
    (map (comp lower-case trim) (split tags-csv ","))))

(defn tags-csv->set [tags-csv]
  (if (nil? tags-csv)
    #{}
    (-> tags-csv tags-csv->list set)))

(defn notebook-tags->set [notebook]
  (let [tags-csv (get-in notebook [:meta :tags])]
    (tags-csv->set tags-csv)))

(defn notebook-tags->list [notebook]
  (let [tags-csv (get-in notebook [:meta :tags])]
    (tags-csv->list tags-csv)))

(defn tag-view
  [tags tag]
  [:span.tag-container
   [:span.tag {:class (when (get tags tag) "active")
               :data-prevent-nav true
               :on-click #(do (.stopPropagation %)
                              (.preventDefault %)
                              (dispatch [:explorer-toggle-tag tag]))} tag]])

(defn tag-box [tags selected-tags]
  [error-boundary
   [:div
    (for [tag tags]
      ^{:key (gensym)} [tag-view selected-tags tag])]])
