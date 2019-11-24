(ns pinkgorilla.explore.list
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [clojure.string :as str]

   [pinkgorilla.storage.storage :refer [gorilla-path]]

   [pinkgorilla.routes :as routes]

   [pinkgorilla.explore.utils :as u]
   [pinkgorilla.explore.ui :as ui]
   ;[open-source.routes :as r]
   [pinkgorilla.explore.form-helpers :as fh]))

(defn filter-tag
  [tags tag]
  [:span.tag-container
   [:span.tag {:class (if (get tags tag) "active")
               :data-prevent-nav true
               :on-click #(do (.stopPropagation %)
                              (.preventDefault %)
                              (dispatch [:toggle-tag tag]))} tag]])

(defn pname [entry]
  (let [name (or (:filename entry) "?")]
    {:name (str/replace name #"^(.+?)/+$" "$1")}))

(defn link [entry]
  (let [storage (:storage entry)]
    (if (nil? storage)
      ""
      (gorilla-path storage)
      )))


(defn view
  []
  (let [listings      (subscribe [:filtered-projects])
        search-input  (fh/builder [:projects :search])
        selected-tags (subscribe [:key :forms :projects :search :data :tags])
        tags          (subscribe [:project-tags])]
    (fn []
      (let [listings @listings
            tags @tags
            selected-tags @selected-tags]
        [:div.list
         [:div.intro
          [:div.main]
          [:div.secondary
           [:button.submit.target
           ; {:on-click #(r/nav "/projects/new")}
            "Create new Notebook"]]]

         [:div.main.listings.public
         ; [ui/ctg {:transitionName "filter-survivor" :class "listing-list"}
          (for [l listings]
            ^{:key (str "os-project-" (:index l))}
            [:div.listing-container
             [:a.listing.clearfix {
                                   :on-click #(routes/nav! (link l))
                                   ;:href (link l)
                                   }

              [:div.core
               [:div.title [ui/attr (pname l) :name]]
               [ui/attr (:meta l) :tagline]
               ;(if (:project/beginner-friendly l)  [:div.beginner-friendly "beginner friendly"])
               (if-let [t (get-in l [:meta :tags])]
                 [:div.tags
                  (for [tag (u/split-tags t)]
                    ^{:key (gensym)} [filter-tag selected-tags tag])])]]])]
          ;]

         [:div.secondary.listings

          [:div.section.search
           [search-input :search :query
            :no-label true
            :placeholder "Search: `music`, `database` ..."]]

          ;[:div.section.beginner-toggle
           ;[search-input :checkbox :project/beginner-friendly :label "Beginner friendly?"]]

          [:div.section.tags
           [:div
            (for [tag tags]
              ^{:key (gensym)} [filter-tag selected-tags tag])]]]]))))
