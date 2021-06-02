(ns pinkgorilla.notebook-ui.app.routes
  (:require
   [pinkgorilla.explorer.default-config :as explorer]
   [goldly.routes :as goldly]))

(def routes-app
  (merge
   explorer/routes-app
   goldly/routes-app
   {""  :ui/notebook-welcome
    "about" :notebook/about ; stays here for testing.
    "nrepl" :ui/nrepl
    "goldly" :notebook/system-list ; notebook theme
    }))

(def routes-api
  (merge
   explorer/routes-api
   goldly/routes-api
   {"nrepl"  :ws/nrepl}))



