(ns pinkgorilla.worksheet.md-segment
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.components.error :refer [error-boundary]]
   [pinkgorilla.components.markdown :refer [markdown]]
   ["react-markdown-editor-lite" :as rmdel]))

(defn md-segment-view
  "markdown segment - view-only mode"
  [seg-data]
  (let [seg-id (:id seg-data)]
    (reagent/create-class
     {:display-name   "markup-segment-view"
      :reagent-render
      (fn [seg-data]
        [:div {:id (name seg-id)
               :class "segment free"}
         [:div.segment-main
          [:div.free-markup
           [markdown (get-in seg-data [:content :value])]]]
         ^{:key :segment-footer}
         [:div.segment-footer]])})))

; awb99: TODO: render css from npm module
; I copied the css from node-modules/react-markdown-editor-lite/lib/index.css
; to resources/css/markdowneditor.css
; 
(defn- markdown-editor
  "markdown editor
   used only by md-segment-edit"
  [segment-id md]
  [error-boundary
   [:<>
    [:link {:rel "stylesheet" :href "/css/markdowneditor.css"}]
    [:> rmdel/default
     {:style {:height 200 ; :width 600
              }
      :value md
      :renderHTML (fn [_ #_text] "<p> Hello Markdown! </p>")
      :config {:view {:menu true
                      :md true
                      :html false}
               :canView {:menu true
                         :md true
                         :html false
                         :fullScreen false
                         :hideMenu false}}
      :onChange (fn [data]
                  (let [data-cljs (js->clj data :keywordize-keys true)]
                    (dispatch [:segment-value-changed segment-id (:text data-cljs)])))}]]])

(defn md-segment-edit
  "markdown segment - edit mode"
  [seg-data]
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        active? (subscribe [:is-active-query seg-id])]
    (reagent/create-class
     {:display-name "markup-segment"
      :reagent-render
      (fn [_] ; seg-data
        [:div {:id (name seg-id)
               :class  (str "segment free"
                            (if @active? " selected" ""))
               :on-click #(dispatch [:worksheet:segment-clicked seg-id])}
         (if @active?
           [markdown-editor seg-id (get-in @segment [:content :value])]
           [markdown (get-in @segment [:content :value])])
         ^{:key :segment-footer}
         [:div.segment-footer]])})))
