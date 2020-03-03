(ns pinkgorilla.output.reagent
  (:require
   ;[taoensso.timbre :refer-macros (info)]
   [pinkgorilla.components.error :refer [error-boundary]]
   [pinkgorilla.ui.pinkie :refer [tag-inject convert-style-as-strings-to-map]]
   [pinkgorilla.ui.widget :refer [resolve-widget]]))

(defn reagent-inject [{:keys [map-keywords widget]} component]
  (let [;_ (info "map-keywords: " map-keywords "widget: " widget " reagent component: " component)
        component (if map-keywords (tag-inject component) component)
        component (if widget (resolve-widget component) component)
        component (if map-keywords (convert-style-as-strings-to-map component) component)
        ;_ (info "inject result: " component)
        ]
    [:div.reagent-output component]))

(defn output-reagent-unsafe
  [output _]
  (let [{:keys [hiccup map-keywords widget]} (:content output)]
    (reagent-inject {:map-keywords map-keywords :widget widget} hiccup)))

(defn output-reagent [output seg-id]
  [error-boundary
   [output-reagent-unsafe output seg-id]])



