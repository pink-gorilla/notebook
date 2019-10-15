(ns pinkgorilla.plugins.hickup
  "plugin to render hickup-style html in pink-gorilla
   (TODO: move to own library)"
  (:require 
   [gorilla-renderable.core :refer :all] ; define Renderable (which has render function)
   [hiccup.core :as hiccup]
   ;[hiccup.page :refer [html5 include-css include-js]]
   ))


(defrecord Hickup [h])
(extend-type Hickup
  Renderable
  (render [h] ; render must return {:type :content}
    {:type :html
     :content (hiccup.core/html (:h h))
     ;:value (pr-str self) ; DO NOT SET VALUE; this will fuckup loading. (at least in original gorilla)
     }))


(defn html! [h]
  "renders hickup-html to a gorilla cell
   syntactical sugar only
   easier to use than to use (Hickup. h)"
  (Hickup. h)
  )

(comment
  
  (render (Hickup. [:h1 "hello"]))
  (hiccup.core/html [:h1 "hello"])
  
  (render (html! [:h1 "hello"]))
  
  )
