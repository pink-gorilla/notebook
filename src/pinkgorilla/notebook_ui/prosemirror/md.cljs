(ns pinkgorilla.notebook-ui.prosemirror.md
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [dispatch]]
   [pinkie.error :refer [error-boundary]]
   ;[webly.user.markdown.views :refer [markdown]]
   ;[pinkgorilla.ui.ui.markdown :refer [markdown]]
   ["marked" :as marked]))


; stolen from gorilla-ui
; but we dont want dependency to it.


(defn ^{:category :ui}
  markdown
  "reagent markdown render component
   usage:
    [markdown markdown-string]"
  [md]
  (if (nil? md) ; marked will crash on (nil? md), so we catch nil. 
    [:p "Empty Markdown"]
    [:div.gorilla-markdown
     {:dangerouslySetInnerHTML
      {:__html (marked md)}}]))

(defn markdown-view [document]
  [:div.free-markup.prose
   [markdown document]])

(defn md-segment-view
  "markdown segment - view-only mode"
  [{:keys [id md]}]
  [:div {; :id id
         :class "segment free prose"}
   [:div.segment-main
    [:div.free-markup
     [markdown md]]]
   ^{:key :segment-footer}
   [:div.segment-footer]])

#_(defn md-segment
    "markdown segment"
    [{:keys [id active?] :as segment}]
    (info "rendering md: " segment)
    [:div {; :id id
           :class  (str "segment free"
                        (if active? " selected" ""))
           :on-click #(dispatch [:notebook/move :to id])}
     (if active?
       [md-segment-edit segment]
       [md-segment-view segment])
     ^{:key :segment-footer}
     [:div.segment-footer]])