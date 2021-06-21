(ns pinkgorilla.notebook-ui.settings.component
  (:require
   [clojure.string :refer [blank?]]
   [taoensso.timbre :as timbre :refer-macros [info]]
   [reagent.core :as r]
   [re-frame.core :as rf]
   [re-com.core :refer [radio-button md-circle-icon-button single-dropdown]]
   [ui.codemirror.theme :as codemirror]
   [pinkgorilla.notebook-ui.settings.default :refer [kernels layouts]]))

; Kernel

(defn radio-kernel [settings key]
  [radio-button
   :label       (name key)
   :value       key
   :model       (:default-kernel @settings)
   :on-change   #(rf/dispatch [:settings/set :default-kernel %])])

(defn kernel [settings]
  (into [:div {:class "md:w-1/4 px-3 mb-6 md:mb-0"}
         [:label {:class "block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2"
                  :for "grid-first-name"}
          "default kernel"]]
        (map (partial radio-kernel settings) kernels)))

; Layout

(defn radio-layout [settings layout-name]
  [radio-button
   :label       (name layout-name)
   :value       layout-name
   :model       (:layout @settings)
   :on-change   #(rf/dispatch [:settings/set :layout %])])

(defn layout [settings]
  (into [:div {:class "md:w-1/4 px-3"}
         [:label {:class "block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2"
                  :for "grid-last-name"}
          "layout"]]
        (map (partial radio-layout settings) layouts)))

; Theme Codemirror

#_(defn radio-theme-codemirror [settings theme-name]
    [radio-button
     :label       theme-name
     :value       theme-name
     :model       (:codemirror-theme @settings)
     :on-change   #(rf/dispatch [:settings/set :codemirror-theme %])])

(def theme-names2
  (doall (map (fn [s] {:id s :label s}) codemirror/themes)))

(defn codemirror-current-theme [settings]
  (let [t (:codemirror-theme settings)
        t (if (or (nil? t) (blank? t))
            "default"
            t)]
    (info "codemirror theme: " t)
    t))

(defn change-theme [t]
  (rf/dispatch [:css/set-theme-component :codemirror t])
  ;(rf/dispatch [:settings/set :codemirror-theme t]
  )

(defn codemirror-theme [settings]
  (let [theme (rf/subscribe [:css/theme-component :codemirror])]
    [:div {:class "md:w-1/4 px-3"}
     [:label {:class "block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2"
              :for "grid-last-name"}
      "codemirror theme"]
     [single-dropdown
      :choices     theme-names2
      :render-fn   (fn [choice] [:div [:span (:label choice)]])
      :model       @theme ; (codemirror-current-theme @settings)
      :placeholder "codemirror-theme"
      :width       "150px"
      :max-height  "300px"
      :filter-box? false
      :on-change    change-theme]]))

(defn settings-edit [settings]
  [:div {:class "bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 flex flex-col my-2"}
   [:p.text-center.text-3xl.mb-4 "Notebook Settings"]
   [:div {:class "-mx-3 md:flex mb-6"}
    [:div {:class "md:w-1/2"} ;.w-20
     [kernel settings]
     [layout settings]]
    [:div
     [codemirror-theme settings]]]])

;; secret management

(defn secret-table  [secrets]
  [:div
   (if (= 0 (count secrets))
     [:p "You currently do not have any secrets stored."]
     [:<>
      [:p.text-red-300 "Click on Secret to remove it."]
      (for [s secrets]
        ^{:key (first s)} [:p.text-blue-600
                           {:on-click #(rf/dispatch [:secret/remove (first s)])}
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
                 :on-click #(do (rf/dispatch [:secret/add @new-secret])
                                (reset! new-secret {:name "" :secret ""}))
                 :class "bg-black text-white font-bold text-md hover:bg-gray-700 p-2 mt-8"}]]])))

(defn upload-file [e]
  (let [file (first (array-seq (.. e -target -files)))
        file-name (.-name file)
        reader (js/FileReader.)]
    (info "uploading secrets from file " file-name)
    (set! (.-onload reader) #(rf/dispatch [:secrets/import (-> % .-target .-result)]))
    (.readAsText reader file)))

(defn upload-secrets []
  [:input {:type "file"
           :name "Import secrets from edn file"
           :on-change #(upload-file %)}])

;; dialog

(defn settings-dialog []
  (let [settings (rf/subscribe [:settings])]
    [:div.bg-white
     ; settings
     [settings-edit settings]

     ; secrets
     [:p {:class "text-center text-3xl"} "Secrets"]
     [secret-table (:secrets @settings)]
     [add-secret]
     [upload-secrets]

     [md-circle-icon-button
      :md-icon-name "zmdi-close"
                   ;;:tooltip "Close"
        ;:on-click (rf/dispatch [:settings/hide])
      ]]))


