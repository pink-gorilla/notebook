(ns pinkgorilla.ui.widget
  "plugin to render widgets in pink-gorilla
   widgets are simply reagent components
   (TODO: move to own library)"
  (:require 
   [gorilla-renderable.core :refer :all] ; define Renderable (which has render function)
   ))


(defn widget! [widget-name & [initial-state]]
  "renders a reagent widget"
  (reify Renderable
    (render [_]
      {:type :widget
       :content {:widget widget-name 
                 :initial-state initial-state}
       ;:value (pr-str self) ; DO NOT SET VALUE; this will fuckup loading. (at least in original gorilla)
       })))




(comment
  (render (widget! "widget.hello"))
  
 )
