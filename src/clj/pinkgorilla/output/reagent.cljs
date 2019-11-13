(ns pinkgorilla.output.reagent
  (:require
   [reagent.core :as reagent :refer [atom]]
   [cljs.reader]
   [widget.hello] ; only included for testing.
   [widget.clock]
   [widget.combo]
   [widget.text]
   [pinkgorilla.ui.vega :refer [vega vegaa]]
   [pinkgorilla.ui.combo :refer [combo]]
   [pinkgorilla.ui.slider :refer [slider]]
   [taoensso.timbre :refer-macros (info)]
   ))


(defn clj->json
  [ds]
  (.stringify js/JSON (clj->js ds)))

(defn widget-not-found
  [name]
  [:div.widget-not-found {:style {:background-color "red"}}
   [:h3 "WIDGET NOT FOUND!"]
   [:p "You need to specify the fully-qualified name of the widget"]
   [:p "Example: widget.hello/world"]
   [:p (str "You have entered: " (clj->json name) )]
   ]
  )


(defn resolve-function [s]
  (let [;pinkgorilla.output.reagentwidget (cljs.core/resolve (symbol widget-name)) ; this is what we want, but resolve is a macro
        _ (info "resolving-function " s)
        ]
    (case s
      widget.clock/binary-clock widget.clock/binary-clock
      "widget.combo/list-selector" widget.combo/list-selector
      widget.hello/world widget.hello/world
      widget.hello/love widget.hello/love
      text widget.text/atom-text
      vega vega
      vegaa vegaa
      combo combo
      slider slider
      widget-not-found)))

(defn resolve-vector [x]
  (let [;_ (info "reagent function found: " x)
        ;_ (info "type of arg: " (type (first (rest x))))
        a [(resolve-function (first x))]
        b (into [] (assoc x 0 (resolve-function (first x))))
        ;b (into [] (assoc x 0 :h1))
        ]
    ;(info "a is: " a)
    ;(info "b is: " b)

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
  (info "found :widget-state: " x)
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
        _ (info "reagent component: " component)
        component (resolve-functions component)
        component (resolve-atoms component)
        _ (info "resolved component: " component)
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
