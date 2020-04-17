(ns pinkgorilla.explore.subscriptions
  (:require
   [re-frame.core :refer [reg-sub subscribe]]
   [pinkgorilla.explore.tags :refer [notebook-tags->list]]
   [pinkgorilla.explore.filter :refer [filter-notebooks]]))

(reg-sub
 :explorer-notebooks-all
 (fn [db _]
   (get-in db [:explorer :notebooks])))

(reg-sub
 :explorer-search-options
 (fn [db _]
   (get-in db [:explorer :search])))

(reg-sub
 :explorer-notebooks-filtered
 (fn [_]
   ;; return a vector of subscriptions
   [(subscribe [:explorer-notebooks-all])
    (subscribe [:explorer-search-options])])
 (fn [[notebooks-all search-options]]
   (filter-notebooks notebooks-all search-options)))

(reg-sub
 :explorer-tags-available
 (fn [_]
   (subscribe [:explorer-notebooks-filtered]))
 (fn [notebooks-filtered]
   (->> notebooks-filtered
        (mapcat notebook-tags->list)
        distinct
        sort)))

