(ns pinkgorilla.dialog.settings
  (:require
    [re-frame.core :refer [subscribe dispatch dispatch-sync]]
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
  (let [settings (subscribe [:settings])
        closefn (fn [event] (dispatch [:app:hide-settings]))]
    (when (:show @settings)
    [modal-panel
     :backdrop-color   "grey"
     :backdrop-opacity 0.4
     :child [v-box :gap "10px"
             :children
             [[label :style {:font-size "18px"} :label "Settings:"]
              [scroller
               :max-height "400px"
               :max-width "600px"
               :child [:p {:style {:fond-size "16px" :width "600px"}}
                       "help is coming!"]]
              [h-box :gap "5px" :justify :end
               :children
               [[md-circle-icon-button
                 :md-icon-name "zmdi-close"
                 :tooltip "Close"
                 :on-click closefn]]]]]])))