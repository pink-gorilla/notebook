(ns pinkgorilla.dialog.settings
  (:require
   [taoensso.timbre :as timbre
    :refer-macros (info)]
   [re-frame.core :refer [subscribe dispatch]]
   [re-com.core
    :refer [h-box v-box scroller md-circle-icon-button
            input-text label radio-button
            modal-panel gap]]
   [pinkgorilla.events.common :refer [reg-set-attr]]
   [pinkgorilla.subs]))

(reg-set-attr ::set-mongodb-ssh [:settings :mongodb-ssh])

(defn extract-result [e]
  (-> e .-target .-result))

(defn save-uploaded-string [e]
  (let [s (extract-result e)]
    (info "mongodb-ssh key uploaded: " s)
    (dispatch [::set-mongodb-ssh s])))

(defn queue-file [e]
  (let [file (first (array-seq (.. e -target -files)))
        file-name (.-name file)
        reader (js/FileReader.)]
    (info "uploading " file-name " ssh key: " (pr-str file))
 ; (info "key is:" (map #(-> e .-target .-result )))
 ;
 ; (put! upload-queue (first (array-seq (.. e -target -files))))
    (set! (.-onload reader) #(save-uploaded-string %))
    (.readAsText reader file)))

(defn settings-dialog []
  (let [dialog (subscribe [:dialog])
        settings (subscribe [:settings])
        closefn (fn [_] (dispatch [:app:hide-settings]))]
    (when (:settings @dialog)
      [modal-panel
       :backdrop-color   "grey"
       :backdrop-opacity 0.4
       :child [v-box :gap "10px"
               :children
               [[label :style {:font-size "18px"} :label "Settings"]

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
                 :label       "parinfer"
                 :value       :parinfer
                 :model       (:editor @settings)
                 :on-change   #(dispatch [:settings-set :editor %])]

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
                ;; [label :label "mongodb-ssh-key (for tunnel)"]
                ;; [gap :size "5px"]
                #_[:input {:type "file" :name "mongodb ssh key"
                           :on-change #(queue-file %)}]

                [scroller
                 :max-height "400px"
                 :max-width "600px"
                 :child [:p {:style {:fond-size "16px" :width "600px"}}
                         "BIG LONG HELP TEXT ON SETTINGS..."]]

                [h-box :gap "5px" :justify :end
                 :children
                 [[md-circle-icon-button
                   :md-icon-name "zmdi-close"
                   ;;:tooltip "Close"
                   :on-click closefn]]]]]])))
