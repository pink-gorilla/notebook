(ns pinkgorilla.notebook-ui.app.pages.nrepl
  (:require
   [webly.web.handler :refer [reagent-page]]
   [pinkgorilla.notebook-ui.nrepl.views.info-page :refer [nrepl-info]]
   [pinkgorilla.notebook-ui.app.menu :refer [home-menu]]))

(defn nrepl-menu []
  [:div
   [:h1 "nrepl info"]
   [nrepl-info]])

(defmethod reagent-page :ui/nrepl [{:keys [route-params query-params handler] :as route}]
  [:<>
   [home-menu]
   [nrepl-menu]])
