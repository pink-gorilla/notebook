(ns pinkgorilla.notebook-ui.app.routes
  (:require
   [pinkgorilla.explorer.default-config :as explorer]))

(def routes-app
  (merge
   explorer/routes-app
   {"nrepl" :ui/nrepl}))

(def routes-api
  (merge
   explorer/routes-api
   {"nrepl"  :ws/nrepl}))



