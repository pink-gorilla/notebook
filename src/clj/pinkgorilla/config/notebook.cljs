(ns pinkgorilla.config.notebook
  "Pinkie is the render system that allows to render reagent komponents with keywords.
   This system needs to be configured, so that all renderers that should be shipped
   with the notebook get loaded. The configuration for this is done here."
  (:require

   ;; PINKIE RENDERERS
   [pinkgorilla.ui.default-setup] ; load renderers from gorilla.renderable-ui
   ; TODO: remove this once we have notebook cljs / clj kernel initialization cells.
   [pinkgorilla.ui.default-renderer] ; load renderers from gorilla.ui
   ; Load renderers defined in the notebook itself
   ; recom widgets: not suitable for self hosted clojurescript
   [pinkgorilla.widget.combo]
   [pinkgorilla.widget.slider]

   ;; NOTEBOOK-REPL
   [pinkgorilla.notebook.repl]))

