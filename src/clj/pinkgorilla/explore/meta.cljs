(ns pinkgorilla.explore.meta
  (:require 
   [re-frame.core :refer [dispatch subscribe]]
   [pinkgorilla.explore.form-helpers :as fh]
   [pinkgorilla.explore.utils :as u]
   [pinkgorilla.explore.ui :as ui]
   ;[open-source.pub.projects.preview :as preview]
   ))

(defn submit-text
  [data]
  (if (:db/id @data)
    "Update"
    "Post"))

(defn form
  [form-path & [source]]
  (let [input             (fh/builder form-path)
        ;show-preview-path (u/flatv :ui form-path :show-preview)
        ;show-preview      (subscribe (into [:key] show-preview-path))
        data              (subscribe (into [:form-data] form-path))
        form              (subscribe (into [:key :forms] form-path))]
    (fn []
      (let [show-preview false ] ;@show-preview]
        [:div.listing-form.clearfix
         ;{:class (if show-preview "show-preview")}
         [:div.form
          ;[:h2 "Details"
          ; ;; TODO make preview less ugly
          ; #_[:div.toggle {:on-click #(dispatch (into [:toggle] show-preview-path))}
          ;    (if show-preview "hide preview" "show preview")]]
          [:form (fh/on-submit form-path)
           [:div
            
            [:div.section.clearfix
             
             ;[input :text :project/name
             ; :required true
             ; :placeholder "luminus"]
             ;[input :text :project/tagline
             ; :tip [:span "A brief description that conveys the project's purpose"
             ;       [:span [ui/markdown-help-toggle]]]
             ; :placeholder "An all-inclusive micro-framework for web dev"]
             ;[input :text :project/repo-url
             ; :required true
             ; :label "Repo URL"
             ; :placeholder "https://github.com/luminus-framework/luminus"
             ; :tip [:span "Where to view the repo in a browser"]]

             ;[input :text :tags
             ; :tip "Comma-separated"
             ; :placeholder "web development, framework, backend, frontend"]

             [input :textarea :project/description
              :placeholder "Luminus is a Clojure micro-framework for web development. We have an active community that's dedicated to helping new-comers. To learn about how to get involved, please visit our contribute page: http://www.luminusweb.net/contribute. You can also stop by #luminus on Slack or IRC."
              :required true
              :tip [:span "What your project does and instructions on how people can get involved."]]]
            ;[:div.field
            ; [:input {:type "submit" :value (submit-text data)}]
            ; [fh/progress-indicator form]]
            
            ]]
          ;[ui/ctg {:transitionName "slide" :class "slide-container"}
           ;(when show-preview [preview/preview data])
          ; ]
          ]]))))


