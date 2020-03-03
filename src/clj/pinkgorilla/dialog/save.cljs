(ns pinkgorilla.dialog.save
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   [re-com.core :refer [input-text radio-button]]
   ;PinkGorilla Notebook
   [pinkgorilla.routes] ;; For defroute
   ))

(defn do-save [form]
  (let [form2 (if (> (count (:file-directory form)) 0)
                (assoc form :filename (str (:file-directory form) "/" (:filename form)))
                form)]
    (dispatch-sync [:save-as-storage form2]) ; sets the :storage in app db
    (dispatch-sync [:save-dialog-cancel]) ; closes the dialog
    (dispatch-sync [:save-notebook]) ; save notebook
    (dispatch-sync [:nav-to-storage]))) ; navigates to the document just saved

(defn check-key [form keycode]
  (case keycode
    27 (dispatch [:save-dialog-cancel]) ; ESC
    13 (do-save form)   ; Enter
    nil))

(defn save-dialog
  []
  (let [config (subscribe [:config])
        dialog (subscribe [:dialog])
        form (reagent/atom {:source :file
                            :description ""
                            :filename ""
                            :repo ""
                            :user ""
                            :explorer-root nil ; the keyword corresponding to the selected explorer root directory
                            :file-directory "" ; directory corresponding to selected explorer root
                            })
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
        (let [file-roots (:explore-file-directories @config)
             ; _ (.log js/console (str "file roots:" file-roots))
              ]
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


            ; file-directory radio


            (when (and (= :file (:source @form))
                       (not (nil? file-roots)))
              [:div.explorer-roots
               [:h3 "File Directory"]
               (doall (map (fn [[location-kw location]]
                             ^{:key location-kw}
                             [radio-button
                              :label       (name location-kw)
                              :value       location-kw
                              :model       (:explorer-root @form)
                              :on-change   #(do (change! :explorer-root location-kw)
                                                (change! :file-directory location))])
                           file-roots))
               [:p (str "Directory: " (:file-directory @form))]])

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
            ]]))})))
