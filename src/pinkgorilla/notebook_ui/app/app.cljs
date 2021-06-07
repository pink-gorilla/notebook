(ns pinkgorilla.notebook-ui.app.app
  (:require

   ; goldly
   [goldly.app]
   [goldly-server.events]
   [goldly-server.pages.about]
   [goldly-server.pages.system]
   [goldly-server.pages.system-list]

   ; explorer
   [pinkgorilla.explorer.default-config]
   ; notebook
   [pinkgorilla.notebook-ui.app.notebook]
   ; notebook app
   [pinkgorilla.notebook-ui.app.events]
   [pinkgorilla.notebook-ui.app.pages.explorer]
   [pinkgorilla.notebook-ui.app.pages.document]
   [pinkgorilla.notebook-ui.app.pages.nrepl]
   [pinkgorilla.notebook-ui.app.pages.about]
   [pinkgorilla.notebook-ui.app.pages.goldly]))
