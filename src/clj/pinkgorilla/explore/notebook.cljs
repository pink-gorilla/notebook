(ns pinkgorilla.explore.notebook
  (:require
   [clojure.string :as str ;:refer [subs]   ; subs should exist, but does not.
    ]
   ;PinkGorilla Libraries
   [pinkgorilla.storage.storage :refer [gorilla-path external-url]]
   ;PinkGorilla Notebook
   [pinkgorilla.routes :as routes]
   [pinkgorilla.explore.tags :refer [tag-box notebook-tags->list]]))

(defn project-name-old [notebook]
  (let [name (or (:filename notebook) "?")]
    {:name (str/replace name #"^(.+?)/+$" "$1")}))

(defn project-path-name
  "extracts the only the name of the file, without extension and path"
  [l]
  (let [full-file-name (:filename l)
        ;file-name (or (:filename notebook) "?")
        ; the regex returns [full-hit name-only]
        name (re-find #"(.+?)([\w-]*).(cljg|ipynb)*$" (or full-file-name ""))]
    name))

(defn notebook-name [l]
  (nth (project-path-name l) 2))

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

(defn notebook-link-gorilla [notebook]
  (let [storage (:storage notebook)]
    (if (nil? storage)
      ""
      (gorilla-path storage))))

(defn storage-link [notebook]
  (let [storage (:storage notebook)]
    (if (nil? storage)
      ""
      (external-url storage))))

(defn tagline [notebook]
  (let [tl (get-in notebook [:meta :tagline])]
    (or tl "No Tagline provided")))


; github stars are not yet included in the view.
; [:div;.stars
;  (:stars l)]


(def border " border-r border-b border-l border-gray-400")
(def lg " lg:border-l-0 lg:border-t lg:border-gray-400 lg:rounded-b-none lg:rounded-r")

(defn notebook-box [selected-tags notebook]
  [:div {:class (str "h-48 bg-green-400 w-1/2 rounded-b  p-4 flex flex-col justify-between leading-normal hover:bg-orange-400" border lg)}

   [:div.mb-8

    ;; project storage location - click opens github web page or the file browser
    [:a {:href (storage-link notebook) :target "_blank" :rel "noopener noreferrer"}
     [:div.px-0.py-0.bg-White
      [:span {:class "pg-storage-prop mr-1"}  (:type notebook)]
      [:span {:class "pg-storage-prop mr-1"} (:repo notebook)]
      [:span {:class "pg-storage-prop"} (project-path notebook)]]]

    ;; project name - click opens the notebook in pink-gorilla
    [:a {:on-click #(routes/nav! (notebook-link-gorilla notebook))}
     [:div {:class "text-white font-bold text-xl mb-2"} (notebook-name notebook)]]

    [:p {:class "text-white text-base h-8 overflow-hidden"}
     (tagline notebook)]]

   [:div.flex.items-center
    [:img {:class "w-10 h-10 bg-white rounded-full mr-4" :src "./pink-gorilla-32.png" :alt "Avatar"}]
    [:div.text-sm.mr-4
     [:p.text-white.leading-none (:user notebook)]
     [:p.text-gray-600 (:edit-date notebook)]]
    [:div.text-sm.mr-4
     [:div.px-6.py-4
      [tag-box (notebook-tags->list notebook) selected-tags]

      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"} "photography"]
      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"} "travel"]
      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700"} "winter"]
      ]]]])

