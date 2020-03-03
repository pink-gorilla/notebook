(ns pinkgorilla.views.renderer
  (:require
   [pinkgorilla.ui.pinkie :refer [renderer-list]]
   [pinkgorilla.notebook.repl :refer [loaded-shadow-namespaces]]))

(defn renderer
  []
  (let [list (renderer-list)
        namespaces (loaded-shadow-namespaces)]
    [:<>
     [:h1 "Loaded Renderers (" (count list) ")"]
     [:div.flex.flex-wrap {:class "w-3/4"}
      [:hr]
      [:table
       [:tbody
        (for [i list]
          ^{:key i} [:tr
                     [:td (:k i)]
                     [:td (:r i)]])]]]
     [:h1 "Loaded Shadow Namespaces (" (count namespaces) ")"]
     [:div.flex.flex-wrap {:class "w-3/4"}
      [:hr]
      [:table
       [:tbody
        (for [i namespaces]
          ^{:key i} [:tr
                     [:td i]])]]]]))




