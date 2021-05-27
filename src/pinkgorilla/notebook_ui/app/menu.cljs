(ns pinkgorilla.notebook-ui.app.menu
  (:require
   [re-frame.core :refer [dispatch]]
   [ui.notebook.tooltip :refer [with-tooltip]]))

(defn header-ico [fa-icon rf-dispatch]
  [:a {:on-click #(dispatch rf-dispatch)
       :class "hover:text-blue-700 mr-1"}
   [:i {:class (str "fa-lg pl-1 " fa-icon)}]])

(defn header-icon [fa-icon rf-dispatch text]
  [with-tooltip text [header-ico fa-icon rf-dispatch]])

(defn home-menu []
  [header-icon "fa fa-question-circle" [:bidi/goto :notebook/about] "notebook/about main page"])