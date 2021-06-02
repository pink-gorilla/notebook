(ns demo.pages.examples
  (:require   
   [webly.web.handler :refer [reagent-page]]
   ; examples
   [demo.views.notebook :refer [codemirror-demo]]
   [demo.views.completion :refer [completion-demo]]
   [demo.views.stacktrace :refer [stacktrace-demo]]
   [demo.views.datafy :refer [datafy-demo]]
   [demo.views.main :refer [main-view]]
   ))


(defmethod reagent-page :demo/main [{:keys [route-params query-params handler] :as route}]
  [main-view])

(defmethod reagent-page :demo/stacktrace [{:keys [route-params query-params handler] :as route}]
  [stacktrace-demo]
  )

(defmethod reagent-page :demo/completion [{:keys [route-params query-params handler] :as route}]
  [completion-demo])

(defmethod reagent-page :demo/datafy [{:keys [route-params query-params handler] :as route}]
  [ datafy-demo])

(defmethod reagent-page :demo/codemirror [{:keys [route-params query-params handler] :as route}]
  [codemirror-demo])