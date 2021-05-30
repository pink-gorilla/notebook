(ns pinkgorilla.notebook-ui.app.routes
  (:require
   [pinkgorilla.explorer.default-config :as explorer]
   [goldly.routes :as goldly]))

(def routes-app
  (merge
   explorer/routes-app
   goldly/routes-app
   {"" :notebook/about
    "x"  :ui/notebook-welcome
    "nrepl" :ui/nrepl}))

(def routes-api
  (merge
   explorer/routes-api
   goldly/routes-api
   {"nrepl"  :ws/nrepl}))



