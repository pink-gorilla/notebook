(ns pinkgorilla.notebook-ui.app.pages.explorer
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :refer [dispatch subscribe]]
   [webly.web.handler :refer [reagent-page]]
   [webly.user.oauth2.view :refer [user-button]]
   [pinkgorilla.document.component :refer [document-page]]
   [pinkgorilla.document.collection.component :refer [notebook-explorer]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]
   [pinkgorilla.notebook-ui.views.notebook :refer [notebook-storage-viewer]]
   [pinkgorilla.notebook-ui.tooltip :refer [with-tooltip]]
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

(defn header-menu-left [storage document]
  [:<>
   ;[:p "str:" ]
   ;[header-icon "fa-question-circle" [:bidi/goto :demo/main]]
   [header-icon "fa fa-question-circle" [:bidi/goto :notebook/about] "notebook/about main page"]
   [header-icon "fa fa-th-large" [:bidi/goto :ui/explorer] "notebook explorer"]
   [header-icon "fa fa-plus" [:document/new] "new notebook"]
   [header-icon "far fa-calendar" [:notebook/clear-all] "clear all output"]
   [header-icon "fa fa-microchip" [:notebook/evaluate-all] "evaluate all code"]
   [header-icon "fa fa-save" [:notebook/save storage] "saves document"]
   [header-icon "fa fa-stream" [:palette/show] "shows keybinding dialog"]
   [nrepl-icon]
   [user-button :github]])

(defn document-viewer [storage document]
  [:div.bg-blue-300.h-full.min-h-full.w-full  ;:div.m-3.bg-blue-300
   [notebook-storage-viewer storage @document]])

(defmethod reagent-page :ui/notebook [{:keys [route-params query-params handler] :as route}]
  [document-page document-viewer header-menu-left query-params])

;; EXPLORER

(defn open-notebook [nb]
  (info "load-notebook-click" nb)
  (goto-notebook! (:storage nb)))

(defmethod reagent-page :ui/explorer [{:keys [route-params query-params handler] :as route}]
  [:<>
   [home-menu]
   [notebook-explorer open-notebook]])
