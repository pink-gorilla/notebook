(ns pinkgorilla.notebook-ui.eval-result.stacktrace
  (:require
   [reepl.helpers :as helpers]))

(def styles
  {:stack-trace {:font-family "monospace"
                 :border-style "solid"
                 :border-width "1px"
                 :border-color "#ff0000"
                 :color "#ff0000"
                 :clear "both"
                 :padding "0.5em 1em 0.5em 1em"
                 :margin-bottom "0.3em"
                 :background-color "white"}})

(def view (partial helpers/view styles))

(defn stacktrace-line [idx {:keys [type file line method flags ns fn]}]
  (let [tooling? (contains? flags :tooling)
        row-classes (str (name type) (when tooling? " tooling-stackframe"))]
    ^{:key idx}
    [:tr {:class row-classes}
     (case type
       :clj [:<>
             [:td [:span.text-blue-900 ns]]
             [:td [:span.text-blue-900 fn]]
             [:td [:span.text-blue-900 (str file ": " line)]]]
       :java [:<>
              [:td]
              [:td [:span.text-green-300 method]]
              [:td [:span.text-green-300 (str file ": " line)]]]
       [:<>
        [:td]
        [:td [:span.text-red-300 method]]
        [:td [:span.text-red-300 (str file ": " line)]]])]))

(defn stacktrace-table [stacktrace]
  [view :stack-trace
     ; Exception
   [:div.text-red-500
    #_[:div {:class "exception-header"} (if err
                                          "An exception was caused by: "
                                          "Exception thrown")]
    #_(when err
        [:span (:class err)])
      ;[:span (:class ex)]
    ]
     ;Stacktrace
   (when stacktrace
     [:table.w-full.text-md.bg-white.shadow-md.rounded.mb-4
      [:tbody
       (map-indexed stacktrace-line stacktrace)]])])


