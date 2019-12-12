(ns pinkgorilla.dialog.save
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   ;; [dommy.core :refer-macros [sel1]]
   [pinkgorilla.routes] ;; For defroute
   [re-com.core :refer [input-text radio-button]]))

(defn do-save [form]
  (dispatch-sync [:save-as-storage form]) ; sets the :storage in app db
  (dispatch-sync [:save-dialog-cancel]) ; closes the dialog
  (dispatch-sync [:save-notebook]) ; save notebook
  (dispatch-sync [:nav-to-storage])) ; navigates to the document just saved

(defn check-key [form keycode]
  (case keycode
    27 (dispatch [:save-dialog-cancel]) ; ESC
    13 (do-save form)   ; Enter
    nil))

(defn save-dialog
  []
  (let [dialog (subscribe [:dialog])
        form (reagent/atom {:source :file
                            :description ""
                            :filename ""
                            :repo ""
                            :user ""})
        change! (fn [k v] (swap! form assoc k v))]
    (reagent/create-class
     {:display-name         "save-dialog"

      ;:component-did-update
      #_(fn [this old-argv]
          (let [el (reagent/dom-node this)]
            (if (:save @dialog)
              (-> el
                  (sel1 :input)
                  .focus))))

      :reagent-render
      (fn []
        [:div.SaveDialog {:style (if-not (:save @dialog) {:display "none"} {})}
         [:div {:class "gorilla-modal-overlay"}]
         [:div {:class "gorilla-modal"}
         ; [:div {:class "modal-content"}

          ; source radio (file/gist/repo)
          [radio-button
           :label       "file"
           :value       :file
           :model       (:source @form)
           :on-change   #(change! :source %)]
          [radio-button
           :label       "gist"
           :value       :gist
           :model       (:source @form)
           :on-change   #(change! :source %)]
          [radio-button
           :label       "repo"
           :value       :repo
           :model       (:source @form)
           :on-change   #(change! :source %)]

          [input-text
           :model           (:user @form)
           :width            "300px"
           :placeholder      "github user name"
           :on-change        #(change! :user %)
           :disabled?        (not (= :repo (:source @form)))]

          [input-text
           :model           (:repo @form)
           :width            "300px"
           :placeholder      "github repo name"
           :on-change        #(change! :repo %)
           :disabled?        (not (= :repo (:source @form)))]

          [input-text
           :model           (:description @form)
           :width            "300px"
           :placeholder      "gist description"
           :on-change        #(change! :description %)
           :disabled?        (not (= :gist (:source @form)))]

          [:h3 "Filename"]
          [:input {:type          "text"
                   :value         (:filename @form)
                                           ;; blur does not work - prevents the click
                                           ;; :on-blur     #(dispatch [:save-dialog-cancel])
                   ; :on-mouse-down #(dispatch [:save-dialog-cancel])
                   :on-key-down   #(check-key @form (.-which %))
                   :on-change     #(change! :filename (-> % .-target .-value))}]

          [:div>div
           {:class    "modal-button"
            :on-click #(dispatch [:save-dialog-cancel])}
           "Cancel"]
          [:div
           {:class    "modal-button highlight"
            :on-click #(do-save @form)}
           "OK"]

         ; ]
          ]])})))
