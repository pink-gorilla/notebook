(ns pinkgorilla.explore.list
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [clojure.string :as str ;:refer [subs]   ; subs should exist, but does not.
    ]

   [pinkgorilla.storage.storage :refer [gorilla-path external-url]]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.explore.utils :as u]
   [pinkgorilla.explore.form-helpers :as fh]))

;; new version:
;; https://github.com/braveclojure/open-source-2/blob/master/src/frontend/open_source/components/project/list.cljs


(defn filter-tag
  [tags tag]
  [:span.tag-container
   [:span.tag {:class (when (get tags tag) "active")
               :data-prevent-nav true
               :on-click #(do (.stopPropagation %)
                              (.preventDefault %)
                              (dispatch [:toggle-tag tag]))} tag]])


;; sidebar on the right (search keyword / tags / etc)


(defn sidebar [search-input tags selected-tags]
  [:div.mt-12

   ;[:div.section.search
   [search-input :search :query
            ;:no-label true
    :placeholder "Search: `music`, `database` ..."]
   ;]

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
    [:div
     (for [tag tags]
       ^{:key (gensym)} [filter-tag selected-tags tag])]]])


;; project details


(defn project-name-old [entry]
  (let [name (or (:filename entry) "?")]
    {:name (str/replace name #"^(.+?)/+$" "$1")}))

(defn project-path-name
  "extracts the only the name of the file, without extension and path"
  [l]
  (let [full-file-name (:filename l)
        ;file-name (or (:filename entry) "?")
        ; the regex returns [full-hit name-only]
        name (re-find #"(.+?)([\w-]*).cljg*$" (or full-file-name ""))]
    name))

(defn project-name [l]
  (last (project-path-name l)))

(defn subs2 [s start]
  (.substring s start (count s)))

(defn project-path [l]
  (let [p (second (project-path-name l))
        root-len (count (:root-dir l))
        ;_ (.log js/console root-len)
        ]
    (if (= (:type l) :file)
      (if (nil? p)
        nil
        (if (nil? root-len)
          p
          (subs2 p root-len))); for local files remove the root dir (we have :repo so dont need full rot path)
      p)))

(defn project-link [entry]
  (let [storage (:storage entry)]
    (if (nil? storage)
      ""
      (gorilla-path storage))))

(defn project-storage-link [entry]
  (let [storage (:storage entry)]
    (if (nil? storage)
      ""
      (external-url storage))))

(defn tagline [entry]
  (let [tl (get-in entry [:meta :tagline])]
    (or tl "No Tagline provided")))


; github stars are not yet included in the view.
; [:div;.stars
;  (:stars l)]


(defn project [selected-tags l]
  [:div {:class "border-r border-b border-l border-gray-400 lg:border-l-0 lg:border-t lg:border-gray-400  rounded-b lg:rounded-b-none lg:rounded-r p-4 flex flex-col justify-between leading-normal"}

   [:div.mb-8

    ;; project storage location - click opens github web page or the file browser
    [:a {:href (project-storage-link l) :target "_blank" :rel "noopener noreferrer"}
     [:div.px-0.py-0.bg-blue-100
      [:span {:class "inline-block bg-gray-200 px-1 py-1 text-sm font-semibold text-gray-700 mr-1"}  (:type l)]
      [:span {:class "inline-block bg-gray-200 px-1 py-1 text-sm font-semibold text-gray-700 mr-1"} (:repo l)]
      [:span {:class "inline-block bg-gray-200 px-1 py-1 text-sm font-semibold text-gray-700"} (project-path l)]]]

    ;; project name - click opens the notebook in pink-gorilla
    [:a {:on-click #(routes/nav! (project-link l))}
     [:div {:class "text-gray-900 font-bold text-xl mb-2"} (project-name l)]]

    [:p {:class "text-gray-700 text-base h-8 overflow-hidden"}
     (tagline l)]]

   [:div.flex.items-center
    [:img {:class "w-10 h-10 rounded-full mr-4" :src "./favicon.ico" :alt "Avatar"}]
    [:div.text-sm.mr-4
     [:p.text-gray-900.leading-none (:user l)]
     [:p.text-gray-600 (:edit-date l)]]
    [:div.text-sm.mr-4
     [:div.px-6.py-4
      (if-let [t (get-in l [:meta :tags])]
        [:div.tags
         (for [tag (u/split-tags t)]
           ^{:key (gensym)} [filter-tag selected-tags tag])])

      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"} "photography"]
      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"} "travel"]
      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700"} "winter"]
      ]]]])


;; the entire explorer page


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

        [:div.flex.w-100 ; separation for main/sidebar

         [:div.flex.flex-wrap {:class "w-3/4"} ;main

         ; [ui/ctg {:transitionName "filter-survivor" :class "listing-list"}
          (for [l listings]
            ^{:key (str "os-project-" (:index l))}
            [:div.h-48.bg-yellow-300.hover:bg-yellow-400 {:class "w-1/2"}
             [project selected-tags l]])]
          ;]
         [:div.bg-gray-400  {:class "w-1/4"} ; sidebar right 1/4 of width
          [sidebar search-input tags]]]))))
