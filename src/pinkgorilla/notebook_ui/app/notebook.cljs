(ns pinkgorilla.notebook-ui.app.notebook
  (:require
   ;; in here are all notebook namespaces that are required for side-effects only

   [pinkgorilla.notebook-ui.settings.events]
   ;[pinkgorilla.notebook-ui.notebook.meta]

   ; eval-result
   [picasso.kernel.protocol]
   [picasso.kernel.view.eval-result]

   ; nrepl kernel
   [picasso.default-config]
   [pinkgorilla.nrepl.kernel.kernel]
   [pinkgorilla.nrepl.kernel.connection] ; connection management
   [pinkgorilla.nrepl.view.info.page] ; reagent-page registration

   ; sniffer 
   [pinkgorilla.notebook-ui.sniffer.events]

   ; notebook

   ;[pinkgorilla.notebook-ui.events.events-snippets]

   [pinkgorilla.notebook-ui.schema.interceptor]

    ;completion
   [pinkgorilla.notebook-ui.completion.component]
   [pinkgorilla.notebook-ui.completion.events]

   ;code-mirror
   [ui.code.goldly.core]

   ; datafy
   [pinkgorilla.notebook-ui.datafy.events-punk]
   [pinkgorilla.notebook-ui.datafy.events-nrepl]
   [pinkgorilla.notebook-ui.datafy.events-dialog]))