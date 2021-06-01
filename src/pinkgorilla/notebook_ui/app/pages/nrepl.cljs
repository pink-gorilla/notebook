(ns pinkgorilla.notebook-ui.app.pages.nrepl
  (:require
   [webly.web.handler :refer [reagent-page]]
   [pinkgorilla.notebook-ui.nrepl.views.info-page :refer [nrepl-info]]
   [pinkgorilla.notebook-ui.app.site :as site]))

(defmethod reagent-page :ui/nrepl [{:keys [route-params query-params handler] :as route}]
  [:<>
   [site/header]
   [:div
    [:h1 "nrepl info"]
    [nrepl-info]]])
