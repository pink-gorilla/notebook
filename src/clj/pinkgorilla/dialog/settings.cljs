(ns pinkgorilla.dialog.settings
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
            modal-panel popover-content-wrapper popover-anchor-wrapper]]))



(defn settings-dialog []
  (let [dialog (subscribe [:dialog])
        settings (subscribe [:settings])
        closefn (fn [event] (dispatch [:app:hide-settings]))]
    (when (:settings @dialog)
      [modal-panel
       :backdrop-color   "grey"
       :backdrop-opacity 0.4
       :child [v-box :gap "10px"
               :children
               [[label :style {:font-size "18px"} :label "Settings"]

                [gap :size "20px"]
                [label :label "github token"]
                [gap :size "5px"]
                [input-text
                 :model           (:github-token @settings)
                 :width            "300px"
                 :placeholder      "github token"
                 :on-change        #(dispatch [:settings-set :github-token %])
                 :disabled?        false]

                [gap :size "20px"]
                [label :label "default kernel"]
                [radio-button
                 :label       "clj"
                 :value       :clj
                 :model       (:default-kernel @settings)
                 :on-change   #(dispatch [:settings-set :default-kernel %])]
                [radio-button
                 :label       "cljs"
                 :value       :cljs
                 :model       (:default-kernel @settings)
                 :on-change   #(dispatch [:settings-set :default-kernel %])]

                [gap :size "20px"]
                [label :label "code text edit mode"]
                [radio-button
                 :label       "text"
                 :value       :text
                 :model       (:editor @settings)
                 :on-change   #(dispatch [:settings-set :editor %])]
                [radio-button
                 :label       "paredit"
                 :value       :paredit
                 :model       (:editor @settings)
                 :on-change   #(dispatch [:settings-set :editor %])]


                [scroller
                 :max-height "400px"
                 :max-width "600px"
                 :child [:p {:style {:fond-size "16px" :width "600px"}}
                         "BIG LONG HELP TEXT ON SETTINGS..."]]

                [h-box :gap "5px" :justify :end
                 :children
                 [[md-circle-icon-button
                   :md-icon-name "zmdi-close"
                   :tooltip "Close"
                   :on-click closefn]]]]]])))
