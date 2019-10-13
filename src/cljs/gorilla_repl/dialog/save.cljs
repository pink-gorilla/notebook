(ns gorilla-repl.dialog.save
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   
   [dommy.core :as dom :refer-macros [sel1]]
   
   ))



(defn save-dialog
  []
  (let [dialog (subscribe [:save-dialog])]
    (reagent/create-class
     {:display-name         "save-dialog"
      :component-did-update (fn [this old-argv]
                              (let [el (reagent/dom-node this)]
                                (if (:show @dialog)
                                  (-> el
                                      (sel1 :input)
                                      .focus))))
      :reagent-render       (fn []
                              [:div.SaveDialog {:style (if-not (:show @dialog) {:display "none"} {})}
                                ;; react-save-template
                               [:div {:class "modal-overlay"}]
                               [:div {:class "modal"}
                                [:h3 "Filename (relative to project directory)"]
                                [:div {:class "modal-content"}
                                 [:input {:type          "text"
                                          :value         (:filename @dialog)
                                           ;; blur does not work - prevents the click
                                           ;; :on-blur     #(dispatch [:save-as-cancel])
                                          :on-mouse-down #(dispatch [:save-as-cancel])
                                          :on-key-down   #(dispatch [:save-as-keydown (.-which %)])

                                          :on-change     #(dispatch [:save-as-change (-> % .-target .-value)])}]
                                  ;; :ref         "filterText"

                                 [:div>div {:class    "modal-button"
                                            :on-click #(dispatch [:save-as-cancel])}
                                  "Cancel"]
                                 [:div {:class    "modal-button highlight"
                                        :on-click #(dispatch [:save-file (:filename @dialog)])}
                                  "OK"]]]])})))