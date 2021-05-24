(ns pinkgorilla.notebook-ui.app.app
  (:require
   [webly.user.app.app :refer [webly-run!]]
   ; picasso
   [picasso.default-config]
   ; goldly
   [goldly.app]
   ; explorer
   [pinkgorilla.explorer.default-config]
   ; notebook
   [pinkgorilla.notebook-ui.app.notebook]
   ; notebook app
   [pinkgorilla.notebook-ui.app.pages.explorer]
   [pinkgorilla.notebook-ui.app.pages.nrepl]
   [pinkgorilla.notebook-ui.app.events]
   [pinkgorilla.notebook-ui.app.routes :refer [routes-api routes-app]]))

(defn ^:export start []
  (webly-run! routes-api routes-app))