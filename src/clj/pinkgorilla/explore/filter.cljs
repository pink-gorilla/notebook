(ns pinkgorilla.explore.filter
  (:require
   [clojure.string :as str]
   [pinkgorilla.explore.tags :refer [notebook-tags->set]]))

(defn- filter-notebooks-text
  [notebooks text]
  (if (empty? text)
    notebooks
    (let [text (str/lower-case text)]
      (filter (fn [notebook]
                (not= -1
                      (.indexOf (->> (vals notebook)
                                     (filter string?)
                                     (str/join " ")
                                     (str/lower-case))
                                text)))
              notebooks))))

(defn- filter-notebooks-tags
  [notebooks tags-set]
  (if (empty? tags-set)
    notebooks
    (filter #(every? (notebook-tags->set %) tags-set)
            notebooks)))

(defn filter-notebooks [notebooks-all search-options]
  (-> notebooks-all
      (filter-notebooks-text (:text search-options))
      (filter-notebooks-tags (:tags search-options))))