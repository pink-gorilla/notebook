(ns pinkgorilla.notebook-ui.app.pages.document
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [reagent.core :as r]
   [re-frame.core :as rf :refer [dispatch subscribe]]
   [webly.web.handler :refer [reagent-page]]
   [webly.user.oauth2.view :refer [user-button]]
   [pinkgorilla.document.component :refer [document-page]]
   [ui.notebook.core :refer [notebook-view]]
   [ui.notebook.tooltip :refer [with-tooltip]]
   [ui.notebook.menu]
   [pinkgorilla.notebook-ui.app.menu :refer [home-menu]]))

(defn header-ico [fa-icon rf-dispatch]
  [:a {:on-click #(dispatch rf-dispatch)
       :class "hover:text-blue-700 mr-1"}
   [:i {:class (str "fa-lg pl-1 " fa-icon)}]])

(defn header-icon [fa-icon rf-dispatch text]
  [with-tooltip text [header-ico fa-icon rf-dispatch]])

;; NOTEBOOK

(defn nrepl-icon []
  (let [connected? (subscribe [:nrepl/connected?])]
    (fn []
      (if @connected?
        [:span.text-green-500 [header-icon "fas fa-circle"  [:bidi/goto :ui/nrepl] "nrepl connected"]]
        [:span.text-red-500 [header-icon "fas fa-circle" [:bidi/goto :ui/nrepl] "nrepl disconnected"]]))))

(defn header-menu-left [storage doc]
  [:<>
   [header-icon "fa fa-question-circle" [:bidi/goto :notebook/about] "notebook/about main page"]
   [header-icon "fa fa-th-large" [:bidi/goto :ui/explorer] "notebook explorer"]

   [ui.notebook.menu/menu]

   [header-icon "fa fa-save" [:notebook/save (get-in doc [:meta :id]) storage] "save document"]
   [header-icon "fa fa-stream" [:palette/show] "shows keybindings and commands"]
  
   [nrepl-icon]
   [user-button :github]])

(def opts
  {; if a layout option is passed this will override the settings in localstorage
   ;:layout :single ; :vertical ; :horizontal
   :view-only true})

(defn document-viewer [storage doc]
  (let [aid (r/atom nil)]
    (fn [storage doc]
      (let [id (get-in doc [:meta :id])]
        (when (not (= @aid id))
          (info "kernel activate doc id: " id)
          (rf/dispatch [:doc/load doc])
          (swap! aid id))
        [:div.bg-blue-300.h-full.min-h-full.w-full  ;:div.m-3.bg-blue-300
         [notebook-view opts]]))))

(defmethod reagent-page :ui/notebook [{:keys [route-params query-params handler] :as route}]
  [document-page document-viewer header-menu-left query-params])


(defmethod reagent-page :ui/notebook-welcome [{:keys [route-params query-params handler] :as route}]
  (let [query-params {:type :res
                      :filename "notebook/clojure/multimethods.cljg"}]
  [document-page document-viewer header-menu-left query-params]))