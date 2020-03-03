(ns pinkgorilla.explore.core
  (:require
   [re-frame.core :refer [subscribe]]
   ;PinkGorilla Notebook
   [pinkgorilla.routes :as routes]
   [pinkgorilla.explore.sidebar :refer [sidebar]]
   [pinkgorilla.explore.notebook :refer [notebook-box]]))

;; new version:
;; https://github.com/braveclojure/open-source-2/blob/master/src/frontend/open_source/components/project/list.cljs


(defn notebook-explorer
  []
  (let [notebooks      (subscribe [:explorer-notebooks-filtered])
        tags-available (subscribe [:explorer-tags-available])
        search         (subscribe [:explorer-search-options])]
    (fn []
      [:div.flex.w-100 ; separation for main/sidebar

       [:div.flex.flex-wrap {:class "w-3/4"} ; notebook grid left, 3/4 of width
         ; [ui/ctg {:transitionName "filter-survivor" :class "listing-list"}
        (for [notebook @notebooks]
          ^{:key (:index notebook)}
          [notebook-box (:tags @search) notebook])]
         ; ]
       [:div  {:class "p-2 w-1/4"} ; sidebar right 1/4 of width
        [sidebar search @tags-available]]])))
