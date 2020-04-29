(ns pinkgorilla.dialog.settings
  (:require
   [taoensso.timbre :as timbre
    :refer-macros (info)]
   [reagent.core :as r]
   [re-frame.core :refer [subscribe dispatch]]
   [re-com.core
    :refer [h-box v-box #_scroller md-circle-icon-button
            #_input-text #_label radio-button
            modal-panel #_gap]]
   ;[pinkgorilla.events.common :refer [reg-set-attr]]
   [pinkgorilla.subs]))

; (reg-set-attr ::set-mongodb-ssh [:settings :mongodb-ssh])

;; Settings

(defn settings-edit [settings]
  [:div {:class "bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 flex flex-col my-2"}
   [:p {:class "text-center text-3xl"} "Settings"]

   [:div {:class "-mx-3 md:flex mb-6"}

    [:div {:class "md:w-1/4 px-3 mb-6 md:mb-0"}
     [:label {:class "block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2"
              :for "grid-first-name"}
      "default kernel"]
     [radio-button
      :label       "clj"
      :value       :clj
      :model       (:default-kernel @settings)
      :on-change   #(dispatch [:settings-set :default-kernel %])]
     [radio-button
      :label       "cljs"
      :value       :cljs
      :model       (:default-kernel @settings)
      :on-change   #(dispatch [:settings-set :default-kernel %])]]

    [:div {:class "md:w-1/4 px-3"}
     [:label {:class "block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2"
              :for "grid-last-name"}
      "code editor mode"]
     [radio-button
      :label       "text"
      :value       :text
      :model       (:editor @settings)
      :on-change   #(dispatch [:settings-set :editor %])]
     [radio-button
      :label       "parinfer"
      :value       :parinfer
      :model       (:editor @settings)
      :on-change   #(dispatch [:settings-set :editor %])]]]])

;; secret management

(defn secret-table  [secrets]
  [:div
   (if (= 0 (count secrets))
     [:p "You currently do not have any secrets stored."]
     [:<>
      [:p.text-red-300 "Click on Secret to remove it."]
      (for [s secrets]
        ^{:key (first s)} [:p.text-blue-600
                           {:on-click #(dispatch [:secret-remove (first s)])}
                           (first s)])])])

(defn add-secret []
  (let [new-secret (r/atom {:name "" :secret ""})]
    (fn []
      [:div {:class "flex flex-row justify-center md:justify-start my-auto pt-8 md:pt-0 px-8 md:px-24 lg:px-32"}
       [:div {:class "flex flex-row pt-4"}
                   ;[:label {:for "name", :class "text-md"} "Name"]
        [:input {:type "text"
                 :placeholder "secret name"
                 :value (:name @new-secret)
                 :on-change #(swap! new-secret assoc :name (-> % .-target .-value))
                 :class "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline text-md"}]]
       [:div {:class "flex flex-row pt-4"}
                   ;[:label {:for "password", :class "text-md"} "Value"]
        [:input {:type "password"
                 :placeholder "secret value"
                 :value (:secret @new-secret)
                 :on-change #(swap! new-secret assoc :secret (-> % .-target .-value))
                 :class "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"}]]
       [:div {:class "flex flex-row pt-4"}
        [:input {:type "submit"
                 :value "Add Secret"
                 :on-click #(do (dispatch [:secret-add @new-secret])
                                (reset! new-secret {:name "" :secret ""}))
                 :class "bg-black text-white font-bold text-md hover:bg-gray-700 p-2 mt-8"}]]])))

(defn upload-file [e]
  (let [file (first (array-seq (.. e -target -files)))
        file-name (.-name file)
        reader (js/FileReader.)]
    (info "uploading secrets from file " file-name)
    (set! (.-onload reader) #(dispatch [:secrets-import (-> % .-target .-result)]))
    (.readAsText reader file)))

(defn upload-secrets []
  [:input {:type "file"
           :name "Import secrets from edn file"
           :on-change #(upload-file %)}])

;; dialog

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
               [[settings-edit settings]

                [:p {:class "text-center text-3xl"} "Secrets"]
                [secret-table (:secrets @settings)]
                [add-secret]
                [upload-secrets]

                ;[scroller
                ; :max-height "400px"
                ; :max-width "600px"
                ; :child [:p {:style {:fond-size "16px" :width "600px"}}
                ;         "BIG LONG HELP TEXT ON SETTINGS..."]]

                [h-box :gap "5px" :justify :end
                 :children
                 [[md-circle-icon-button
                   :md-icon-name "zmdi-close"
                   ;;:tooltip "Close"
                   :on-click closefn]]]]]])))
