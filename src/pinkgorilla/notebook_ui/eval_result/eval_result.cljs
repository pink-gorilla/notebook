(ns pinkgorilla.notebook-ui.eval-result.eval-result
  (:require
   [pinkgorilla.notebook-ui.eval-result.console :refer [console-view]]
   [pinkgorilla.notebook-ui.eval-result.error :refer [error-text]]
   [pinkgorilla.notebook-ui.eval-result.stacktrace :refer [stacktrace-table]]
   [pinkgorilla.notebook-ui.eval-result.picasso :refer [picasso-cell]]
   [pinkgorilla.notebook-ui.eval-result.datafy :refer [datafy-link]]))

(defn eval-result-view-pure [{:keys [picasso out stacktrace err root-ex datafy]}] ; :as result
  [:<>

   ; for performance it is important that all sub eements only get the fields they need
   ; for rendering. Otherwise they will re-render when the data of other elements change.

   (when err
     [error-text err root-ex])
   (when stacktrace
     [stacktrace-table stacktrace])

   (when datafy
     [datafy-link datafy])

   (when out
     [console-view out])

   (if picasso
     [picasso-cell picasso]
     [:div.flex-grow-1 ""])])

(defn eval-result-view [result]
  [:div.w-full.h-full.bg-gray-200.flex.flex-col.flex-grow-1.eval-result ; .mt-2
   [eval-result-view-pure result]])
