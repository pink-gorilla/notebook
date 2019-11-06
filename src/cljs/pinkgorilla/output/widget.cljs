(ns pinkgorilla.output.widget
  (:require
   [reagent.core :as reagent]
   [widget.hello] ; only included for testing.
   [widget.clock]
   [widget.combo]
   [taoensso.timbre :refer-macros (info)]
   ))


(defn widget-not-found
  [name]
  [:div.widget-not-found {:style {:background-color "red"}}
   [:h3 "WIDGET NOT FOUND!"]
   [:p "You need to specify the fully-qualified name of the widget"]
   [:p "Example: widget.hello/world"]
   [:p "Example: widget.clock/binary-clock"]
   [:p (str "You have entered: " name) ]
   ]
  )


(defn name-to-reagent [widget-name]
  (let [;widget (cljs.core/resolve (symbol widget-name)) ; this is what we want, but resolve is a macro
        ]
    (case widget-name
      "widget.clock/binary-clock" widget.clock/binary-clock
      "widget.combo/list-selector" widget.combo/list-selector
      "widget.hello/world" widget.hello/world
      nil)))


;; TODO Ugh, old stylesheets persist as html so we get a string
(defn output-widget
  [output _]
  (let [content (:content output)
        _ (info "content: " content)
        initial-state (:initial-state content)
        state (reagent/atom initial-state)
        widget-name (:widget content)
        widget (name-to-reagent widget-name)
        ]
        (reagent/create-class
         {:display-name "output-widget"
          :reagent-render
          (fn []
            [:div.widget
             (if (nil? widget)
               [widget-not-found widget-name]
               (if (nil? initial-state)
                 [widget] ;[widget.hello/world]
                 [widget state] ;[widget.hello/world]
                 )
               )
             [:p (str "widget: " widget-name " initial-state: " @state)]])})))
