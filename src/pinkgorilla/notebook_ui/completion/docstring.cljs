(ns pinkgorilla.notebook-ui.completion.docstring
  (:require
   [cljs.reader]
   [cljs.tools.reader]
   [reagent.core :as r]
   [reepl.helpers :as helpers]
   [pinkie.pinkie :refer-macros [register-component]]))

(def styles
  {:docs {;:height 200
          :overflow :auto
          :padding "5px 10px"}
   :docs-empty {:color "#ccc"
                :padding "5px 10px"}})

(def view (partial helpers/view styles))

(defn docs-view [docs]
  [:div.f-full.w-full

   {:style {:font-family "monospace"
                 ;:flex 1
                 ;:display :flex
            :white-space "pre-wrap"
            :text-align "left"}}
   [view :docs
    (or docs [view :docs-empty "This is where docs show up"])]])