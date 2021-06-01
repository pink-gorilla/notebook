(ns pinkgorilla.notebook-ui.app.pages.goldly
  (:require
   [webly.web.handler :refer [reagent-page]]
   [goldly-server.pages.system-list :refer [systems-list-page]]
   [goldly.system :refer [system-ext]]
   [pinkgorilla.notebook-ui.app.site :refer [header]]))

(defmethod reagent-page :notebook/system-list [{:keys [route-params query-params handler] :as route}]
  [:div
   [header]
   [systems-list-page]])

(defn system-themed [id ext]
  [:div
   [header]
   [:div.container.mx-auto ; tailwind containers are not centered by default; mx-auto does this
    [:p.mt-5.mb-5.text-purple-600.text-3xl id]
    [system-ext id ext]]])

(defmethod reagent-page :goldly/system [{:keys [route-params query-params handler] :as route}]
  ;(info "loading system" route-params)
  [system-themed (:system-id route-params) ""])

(defmethod reagent-page :goldly/system-ext [{:keys [route-params query-params handler] :as route}]
  ;(info "loading system-ext" route-params)
  [system-themed (:system-id route-params) (:system-ext route-params)])