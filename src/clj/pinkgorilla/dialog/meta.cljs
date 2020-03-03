(ns pinkgorilla.dialog.meta
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [re-com.core
    :refer [h-box v-box gap md-circle-icon-button input-text label modal-panel]]

   ;; [pinkgorilla.explore.meta :refer [form]]
   ))

(defn meta-dialog []
  (let [dialog (subscribe [:dialog])
        meta (subscribe [:meta])
        closefn (fn [_] (dispatch [:dialog-hide :meta]))]
    (when (:meta @dialog)
      [modal-panel
       :backdrop-color   "grey"
       :backdrop-opacity 0.4
       :child [v-box :gap "10px"
               :children
               [[:h1 "Notebook Meta Data"]

                [gap :size "20px"]
                [label :label "tagline (brief description)"]
                [gap :size "5px"]
                [input-text
                 :model           (:tagline @meta)
                 :width            "300px"
                 :placeholder      "tagline"
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
                   ;; :tooltip "Close"
                   :on-click closefn]]]]]])))




