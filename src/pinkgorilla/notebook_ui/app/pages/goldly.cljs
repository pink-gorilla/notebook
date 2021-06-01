(ns pinkgorilla.notebook-ui.app.pages.goldly
  (:require
   [webly.web.handler :refer [reagent-page]]
   [goldly-server.pages.system-list :refer [systems-list-page]]
   [pinkgorilla.notebook-ui.app.site :refer [header]]))

(defmethod reagent-page :notebook/system-list [{:keys [route-params query-params handler] :as route}]
  [:div
   [header]
   [systems-list-page]])