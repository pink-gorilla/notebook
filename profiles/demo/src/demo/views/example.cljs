(ns demo.views.example
  (:require
   [pinkie.error :refer [error-boundary]]
   [pinkie.pinkie :refer [tag-inject]]
   [demo.views.sidebar :refer [sidebar]]))

(defonce examples (atom []))

(defn example-page [name component]
  ;[:div.flex.flex-col.w-full.h-full ; {:style {:background-color "yellow"}}
   ;[:h1.mb-5 name]
  [:div.w-full.h-full
   ;[error-boundary
    [component]
     ;(tag-inject component)
    ]
;  ]
)


(defn add [name component]
  (swap! examples conj {:name name 
                        :page (example-page name component)}))


(defn examples-component [page-default]
  [:div
   [sidebar @examples page-default]])
