(ns pinkgorilla.components.markdown
  "markdown syntax requires either js library or react component
   this namespace exists to keep the dependencies together."
  (:require
   ["marked" :as marked]))

(defn markdown
  "reagent markdown render component
   implemented via js/marked (required via package.json / shadow-cljs)
   
   Notes: marked will crash on (nil? md), so we catch nil.       
  "
  [md]
  (if (nil? md)
    [:h1 "Empty Markdown"]
    [:div.gorilla-markdown
     {:dangerouslySetInnerHTML
      {:__html (marked md)}}]))

;; awb99:
;; 
;; I tried two react components:
;; 
;; 1. terra-markdown
;;    ["terra-markdown" :as react-md]
;;    
;; 2. react-markdown
      ;["react-markdown" :as react-md]
   ; react-markdown requires npm dependency: 
   ; unist-util-visit ^1.3.0
   ; unist-util-visit-parents@^2.0.0

#_(defn markdown [md]
    [:div.gorilla-markdown
     [:> react-md {:src md :id "bongo"}]])

#_(println "react-md: " (pr-str react-md))

