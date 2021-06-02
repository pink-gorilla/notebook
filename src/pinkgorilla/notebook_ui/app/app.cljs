(ns pinkgorilla.notebook-ui.app.app
  (:require
   [webly.user.app.app :refer [webly-run!]]

   ; goldly
   [goldly.app]
   [goldly-server.app] ; pages
   ; explorer
   [pinkgorilla.explorer.default-config]
   ; notebook
   [pinkgorilla.notebook-ui.app.notebook]
   ; notebook app
   [pinkgorilla.notebook-ui.app.events]
   [pinkgorilla.notebook-ui.app.routes :refer [routes-api routes-app]]
   [pinkgorilla.notebook-ui.app.pages.explorer]
   [pinkgorilla.notebook-ui.app.pages.document]
   [pinkgorilla.notebook-ui.app.pages.nrepl]
   [pinkgorilla.notebook-ui.app.pages.about]
   [pinkgorilla.notebook-ui.app.pages.goldly]))

(defn ^:export start []
  (webly-run! routes-api routes-app))