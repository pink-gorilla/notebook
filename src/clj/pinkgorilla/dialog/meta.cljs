(ns pinkgorilla.dialog.meta
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [re-com.core
    :as rcm
    :refer [h-box v-box box border gap line h-split v-split scroller
            button row-button md-icon-button md-circle-icon-button info-button
            input-text input-password input-textarea
            label title p
            single-dropdown selection-list
            checkbox radio-button slider progress-bar throbber
            horizontal-bar-tabs vertical-bar-tabs
            modal-panel popover-content-wrapper popover-anchor-wrapper]]

   [pinkgorilla.explore.meta :refer [form]]))



(defn meta-dialog []
  (let [dialog (subscribe [:dialog])
        meta (subscribe [:meta])
        closefn (fn [event] (dispatch [:dialog-hide :meta]))]
    (when (:meta @dialog)
      [modal-panel
       :backdrop-color   "grey"
       :backdrop-opacity 0.4
       :child [v-box :gap "10px"
               :children
               [[:h1 "Notebook Meta Data"]

                [gap :size "20px"]
                [label :label "project name"]
                [gap :size "5px"]
                [input-text
                 :model           (:name @meta)
                 :width            "300px"
                 :placeholder      "notebook name"
                 :on-change        #(dispatch [:meta-set :name %])
                 :disabled?        false]

                [gap :size "20px"]
                [label :label "brief descripton"]
                [gap :size "5px"]
                [input-text
                 :model           (:tagline @meta)
                 :width            "300px"
                 :placeholder      "brief description"
                 :on-change        #(dispatch [:meta-set :tagline %])
                 :disabled?        false]

                [gap :size "20px"]
                [label :label "tags (comma separated)"]
                [gap :size "5px"]
                [input-text
                 :model           (:tags @meta)
                 :width            "300px"
                 :placeholder      "tags, comma separated"
                 :on-change        #(dispatch [:meta-set :tags %])
                 :disabled?        false]

                [gap :size "20px"]
                [label :label "long descripton"]
                [gap :size "5px"]
                [input-text
                 :model           (:description @meta)
                 :width            "300px"
                 :placeholder      "long description"
                 :on-change        #(dispatch [:meta-set :description %])
                 :disabled?        false]

                ;[form [:worksheet :meta]]


                [h-box :gap "5px" :justify :end
                 :children
                 [[md-circle-icon-button
                   :md-icon-name "zmdi-close"
                   :tooltip "Close"
                   :on-click closefn]]]]]])))




