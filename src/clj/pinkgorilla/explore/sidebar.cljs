(ns pinkgorilla.explore.sidebar
  (:require
   [re-frame.core :refer [dispatch]]
   ;PinkGorilla Notebook
   [pinkgorilla.routes :as routes]
   [pinkgorilla.explore.tags :refer [tag-box]])
  (:require-macros [pinkgorilla.components.utils :refer [tv]]))

(defn sidebar
  "sidebar on the right (search keyword / tags / etc)"
  [search tags-available]
  [:div.mt-12

   [:h3 "Search Text"]
   [:input {:value (:text @search)
            :on-change #(dispatch [:explorer-search-text (tv %)])
            :placeholder "Text Search: `leaflet`, `ISS` ..."}]

   #_[:div
      [:label.label {:for "stargazers-count"}]
      [:span
       [:i.fa.fa-star]
       "min github stars"]
      [:div
       [:input.input.stargazers-count {:type "number"
                                       :label "span,i.fa.fa-star, min github stars"
                                       :id "stargazers-count"
                                       :value "5"}]]]

   #_[:div
      [:label.label {:for "days-since-push"}
       [:span
        [:i.fa.fa-clock-o]
        "most days since last edit"]]
      [:div
       [:input.input.days-since-push {:type "number"
                                      :label "span,i.fa.fa-clock-o, most days since last push"
                                      :id "days-since-push"
                                      :value "368"}]]]

   [:div.section.tags
    [tag-box tags-available (:tags @search)]]])
