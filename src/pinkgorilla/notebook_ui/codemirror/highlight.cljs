(ns pinkgorilla.notebook-ui.codemirror.highlight
  (:require
   [reagent.dom :as rd]
   [reagent.core :as r]
   ["codemirror" :as CodeMirror]))

(defn colored-text [text style]
  (r/create-class
   {:component-did-mount
    (fn [this]
      (let [node (rd/dom-node this)]
        ((aget CodeMirror "colorize") #js[node] "clojure")))
    :reagent-render
    (fn [_]
      [:pre {:style (merge {:padding 0 :margin 0} style)}
       text])}))