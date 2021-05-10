(ns pinkgorilla.notebook-ui.views.layout
  (:require
   [pinkgorilla.notebook-ui.codemirror.theme :refer [style-codemirror-fullscreen style-codemirror-inline]]
   [pinkgorilla.notebook-ui.completion.component :refer [completion-component]]
   [pinkgorilla.notebook-ui.views.segment-nav :refer [segment-nav]]
   [pinkgorilla.notebook-ui.views.segment :refer [segment-input segment-output segment-output-no-md]]))

(defn layout-vertical [eval-result]
  [:div.w-full ; .mt-5.p-2
   [segment-input eval-result]
   ;[segment-output eval-result]
   [segment-output-no-md eval-result]])

(defn layout-horizontal [eval-result]
  [:div.w-full.flex.flex-row ; .mt-5.p-2
   [:div {:class "w-1/2"}
    [segment-input eval-result]]
   [:div {:class "w-1/2"}
    [segment-output eval-result]]])

(defn notebook-layout-single [{:keys [id] :as segment}]
  [:div.flex.flex-row.w-full.h-full.min-h-full.bg-yellow-400.items-stretch
   [style-codemirror-fullscreen]
    ; LEFT: code-mirror / code-completion
   [:div.bg-yellow-500.h-full.flex.flex-col {:class "w-1/2"}
    [segment-nav]
    [:div.w-full.bg-red-300.flex-grow
      ;{:style {:height "600px"}}
     [segment-input segment]]
    [:div.h-40.w-full.bg-teal-300
     [completion-component]]]
      ; RIGHT: error / console / result
   [:div.bg-blue-100.h-full.flex.flex-col
    {:class "w-1/2"
     :style {:overflow-y "auto"}}
    [:div.flex-grow  ; flex-grow scales the element to remaining space
     [segment-output segment]]]])

(defn notebook-layout-multiple [layout segments]
  (let [segment-layout (case layout
                         :vertical   layout-vertical
                         :horizontal layout-horizontal
                         layout-vertical)]
    [:div.h-full.min-h-full
     [style-codemirror-inline]
     (for [s segments]
       ^{:key (:id s)}
       [segment-layout s])]))
;; => nil


(defn notebook-layout-stacked [segments]
  [:div.flex.flex-row.w-full.h-full.bg-yellow-400.stackednotebook.overflow-y-auto.max-h-full
   [:div.stackedinput.overscroll-y-auto.overflow-y-scroll {:class "w-1/2"}
    [style-codemirror-inline]
    (for [s segments]
      ^{:key (:id s)}
      [segment-input s])]
   [:div.stackedoutput.overscroll-y-auto.overflow-y-scroll {:class "w-1/2"}
    (for [s segments]
      ^{:key (:id s)}
      [segment-output s])]])
;; => nil


(defn notebook-layout [{:keys [layout]
                        :or {layout :vertical}
                        :as settings}
                       segment-active segments]
  (case layout
    :single
    [notebook-layout-single segment-active]

    :stacked
    [notebook-layout-stacked segments]

    [notebook-layout-multiple layout segments]))