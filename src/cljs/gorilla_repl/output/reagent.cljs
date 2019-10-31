(ns gorilla-repl.output.reagent
  (:require
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   
   [widget.hello] ; only included for testing.
   [widget.clock]
   [widget.combo]
   [widget.text]
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


(defn resolve-function [s]
  (let [;gorilla-repl.output.reagentwidget (cljs.core/resolve (symbol widget-name)) ; this is what we want, but resolve is a macro
        _ (println "resolving-function " s)
        ]
    (case s
      widget.clock/binary-clock widget.clock/binary-clock
      "widget.combo/list-selector" widget.combo/list-selector
      widget.hello/world widget.hello/world
      widget.hello/love widget.hello/love
      text widget.text/atom-text
      widget-not-found)))

(defn resolve-vector [x]
  (let [;_ (println "reagent function found: " x)
        ;_ (println "type of arg: " (type (first (rest x))))
        a [(resolve-function (first x))]
        b (into [] (assoc x 0 (resolve-function (first x))))
        ;b (into [] (assoc x 0 :h1))
        ]
    ;(println "a is: " a)
    ;(println "b is: " b)
    
    ;a
    b
    ))

(def state-atom
  (reagent/atom 
   {:name "bongo" 
    :time "5 before 12"}))



(defn resolve-functions
  "resolve function-as symbol to function references in the reagent-hickup-map.
   Leaves regular hiccup data unchanged."
  [reagent-hiccup-syntax]
  (clojure.walk/prewalk
    (fn [x] 
      (if (and (coll? x) (symbol? (first x)))
          (resolve-vector x)
          x))
    reagent-hiccup-syntax))

(defn resolve-state [x]
  (println "found :widget-state: " x)
  state-atom
  )

(defn resolve-atoms
  "resolve function-as symbol to function references in the reagent-hickup-map.
   Leaves regular hiccup data unchanged."
  [reagent-hiccup-syntax]
  (clojure.walk/prewalk
   (fn [x]
     (if (= x :widget-state) 
        (resolve-state x)
       x))
   reagent-hiccup-syntax))


(defn output-reagent
  [output _]
  (let [content (:content output)
        component (cljs.reader/read-string (:value output))
        _ (println "reagent component: " component)
        component (resolve-functions component)
        component (resolve-atoms component)
        ;_ (println "resolved component: " component)
        ;initial-state (:initial-state content)
        ;state (reagent/atom initial-state)
        
        ;widget (name-to-reagent widget-name)
        ]
        (reagent/create-class 
         {:display-name "output-reagent"
          :reagent-render (fn []
                            [:div.reagent
                             component
                             [:p (str "state: " @state-atom)]]
                             )})))
