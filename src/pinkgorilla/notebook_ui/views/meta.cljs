(ns pinkgorilla.notebook-ui.views.meta
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [subscribe dispatch reg-event-db]]
   [re-com.core :refer [h-box v-box gap md-circle-icon-button input-text label]]
   [pinkgorilla.ui.config :refer [link-css]]))

(defn meta-dialog []
  (let [notebook (subscribe [:notebook])
        meta  (:meta @notebook)]
    [:div.bg-blue-300
     [v-box :gap "10px"
      :children
      [[:h1 "Notebook Meta Data"]

       [gap :size "20px"]
       [label :label "tagline (brief description)"]

       [gap :size "5px"]
       [input-text
        :model           (:tagline meta)
        :width            "300px"
        :placeholder      "tagline"
        :on-change        #(dispatch [:notebook/meta-set :tagline %])
        :disabled?        false]

       [gap :size "20px"]
       [label :label "tags (comma separated)"]
       [gap :size "5px"]
       [input-text
        :model           (:tags meta)
        :width            "300px"
        :placeholder      "tags, comma separated"
        :on-change        #(dispatch [:notebook/meta-set :tags %])
        :disabled?        false]

       #_[gap :size "20px"]
       #_[label :label "long descripton"]
       #_[gap :size "5px"]
       #_[input-text
          :model           (:description meta)
          :width            "300px"
          :placeholder      "long description"
          :on-change        #(dispatch [:notebook/meta-set :description %])
          :disabled?        false]

       [h-box :gap "5px" :justify :end
        :children
        [[md-circle-icon-button
          :md-icon-name "zmdi-close"
          :on-click (fn [_] (dispatch [:modal/close]))]]]]]]))

(reg-event-db
 :notebook/meta-show
 (fn [db [_]]
   (info "showing meta dialog")
   (dispatch [:modal/open [meta-dialog] :medium])
   db))