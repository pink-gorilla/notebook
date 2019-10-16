(ns pinkgorilla.ui.hickup
  "plugin to render hickup-style html in pink-gorilla
   (TODO: move to own library)"
  (:require 
   [gorilla-renderable.core :refer :all] ; define Renderable (which has render function)
   [hiccup.core :as hiccup]
   [hiccup.page :refer [html5 include-css include-js]]
   ))


;; This implementation uses reify. This means we do not need a dedicated defrecord to get the rendering done.
;; So this kind of structure is cleaner.
(defn html! [hickup-data]
  "renders hickup as html in a gorilla cell"
  (reify Renderable
    (render [_]
      {:type :html
       :content (hiccup.core/html hickup-data)
       ;:value (pr-str self) ; DO NOT SET VALUE; this will fuckup loading. (at least in original gorilla)
       })))


;; OLD CODE FOLLOWS
;; This code does exactly the same as above, except that it defines and overrides Hickup record.
;; 2019 10 16 awb99: is there any reason to keep this, or should it just be removed?

(defrecord Hickup [h])
(extend-type Hickup
  Renderable
  (render [h] ; render must return {:type :content}
    {:type :html
     :content (hiccup.core/html (:h h))
     ;:value (pr-str self) ; DO NOT SET VALUE; this will fuckup loading. (at least in original gorilla)
     }))


(defn old-html! [h]
  "renders hickup-html to a gorilla cell
   syntactical sugar only
   easier to use than to use (Hickup. h)"
  (Hickup. h)
  )

;;

(defn bongo []
  (html! [:div
          [:p "bongo"]
          (include-js "https://code.highcharts.com/highcharts.js")          
          ]))
 
  




(comment
  
  (render (Hickup. [:h1 "hello"]))
  (hiccup.core/html [:h1 "hello"])
  
  (render (html! [:h1 "hello"]))
  
  (render (html2! [:h1 "hello"]))
  
  )
