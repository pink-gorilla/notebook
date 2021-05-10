(ns pinkgorilla.notebook-ui.eval-result.console
  (:require
   [clojure.string :as str]
   [reagent.dom]
   [pinkie.text :refer [text]]
   [reepl.helpers :as helpers]))

(def styles
  {:console-text {:padding "0.5em 1em 0.5em 1em"
                  :margin-bottom "0.3em"
                  :font-family "monospace"
                  :font-size "12px"
                  :color "steelblue"
                  :border-color "#cccccc"
                  :border-style "solid"
                  :border-width "1px"
                  :background-color "#FFFFFF"
                  :word-wrap "break-word"}

   :console-text_pre {; not yet sure how to get this
                      :white-space "pre-wrap"}})

(def view (partial helpers/view styles))

(defn console-text [txt]
  [view :console-text
   [text txt]])

#_(defn console-text-textarea [c]
    [:textarea.font-mono.text-left.w-full.text-gray-700 ; .bg-opacity-0 ;.bg-blue-700 ; .yellow-300.w-full
     {:rows 5
     ;:cols 80
      :style {;:overflow-x "scroll"
              :overflow-x "hidden"
              :white-space "pre"
              :background-color "transparent"}
      :value c
      :readOnly true
     ;:defaultValue c
      }])

(defn console-view [c]
  (when-not (str/blank? c)
    [:div.w-full  ; .gray-900
     [console-text c]]))