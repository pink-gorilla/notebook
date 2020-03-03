(ns pinkgorilla.worksheet.code-stacktrace
  (:require
   [reagent.dom]
   [pinkgorilla.ui.text :refer [text]]))

(defn error-text [text]
  [:div.error-text text])

(defn console-text [txt]
  [:div.console-text
   [text txt]])

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

(defn stacktrace-table [e]
  (let [ex (if (:cause e) (:cause e) e)]
    [:div {:class "stack-trace"}
     ; Exception
     [:div.exception
      [:div {:class "exception-header"} (if (:cause e)
                                          "An exception was caused by: "
                                          "Exception thrown")]
      [:span (:class ex)]
      [:span (:class ex)]]
     ;Stacktrace
     [:table.w-full.text-md.bg-white.shadow-md.rounded.mb-4
      [:tbody
       (map-indexed stacktrace-line (:stacktrace ex))]]]))


