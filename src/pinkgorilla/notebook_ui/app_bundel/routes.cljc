(ns pinkgorilla.notebook-ui.app-bundel.routes
  (:require
   [pinkgorilla.notebook-ui.app.routes :as notebook]
   [goldly.routes :as goldly]))

(def routes-app
  (merge
   notebook/routes-app
   goldly/routes-app
   {"" :notebook/about
    ["examples/" :category] :gorilla-ui/example}))

(def routes-api
  (merge
   notebook/routes-api
   goldly/routes-api))



