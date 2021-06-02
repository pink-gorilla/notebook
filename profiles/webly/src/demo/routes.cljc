(ns demo.routes
  (:require
   [pinkgorilla.notebook-ui.app.routes]))

(def routes-app
  (assoc pinkgorilla.notebook-ui.app.routes/routes-app
         "" :notebook/about
         ))

(def routes-api
   pinkgorilla.notebook-ui.app.routes/routes-api)
