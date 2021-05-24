(ns pinkgorilla.notebook-ui.app.notebook
  (:require
   ;; in here are all notebook namespaces that are required for side-effects only

   [pinkgorilla.notebook-ui.settings.events]
   ;[pinkgorilla.notebook-ui.notebook.meta]

   ; eval-result
   [picasso.kernel.protocol]
   [picasso.kernel.view.eval-result]

   ; kernel
   [pinkgorilla.notebook-ui.kernel.events]
   [pinkgorilla.notebook-ui.kernel.events-sniffer]

   ; nrepl kernel
   [pinkgorilla.notebook-ui.nrepl.events.connection]
   [pinkgorilla.notebook-ui.nrepl.events.op]
   [pinkgorilla.notebook-ui.nrepl.events.eval]
   [pinkgorilla.notebook-ui.nrepl.events.info]
   [pinkgorilla.notebook-ui.nrepl.subscriptions]
   [pinkgorilla.notebook-ui.nrepl.views.info-page] ; reagent-page registration

   ; notebook

   [pinkgorilla.notebook-ui.events.events-edit]
   [pinkgorilla.notebook-ui.events.events-move]
  ; [pinkgorilla.notebook-ui.events.events-segment]
   [pinkgorilla.notebook-ui.events.events-snippets]
   [pinkgorilla.notebook-ui.schema.interceptor]

   ;[pinkgorilla.notebook-ui.notebook.notebook]


    ;completion
   [pinkgorilla.notebook-ui.completion.component]
   [pinkgorilla.notebook-ui.completion.events]

   ;code-mirror
   [ui.code.goldly.core]
   ;[ui.codemirror.events.core]
   ;[ui.codemirror.events.completion]

   ; datafy
   [pinkgorilla.notebook-ui.datafy.events-punk]
   [pinkgorilla.notebook-ui.datafy.events-nrepl]
   [pinkgorilla.notebook-ui.datafy.events-dialog]))