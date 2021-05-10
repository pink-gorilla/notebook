(ns pinkgorilla.notebook-ui.app.notebook
  (:require
   ;; in here are all notebook namespaces that are required for side-effects only

   [pinkgorilla.notebook-ui.settings.events]
   ;[pinkgorilla.notebook-ui.notebook.meta]

   ; eval-result
   [pinkgorilla.notebook-ui.eval-result.stacktrace]
   [pinkgorilla.notebook-ui.kernel.picasso]

   ; kernel
   [pinkgorilla.notebook-ui.kernel.events]
   [pinkgorilla.notebook-ui.kernel.events-mock]
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
   [pinkgorilla.notebook-ui.events.events-segment]
   [pinkgorilla.notebook-ui.events.events-eval]
   [pinkgorilla.notebook-ui.events.events-snippets]

   [pinkgorilla.notebook-ui.schema.interceptor]

   ;[pinkgorilla.notebook-ui.notebook.notebook]
   [pinkgorilla.notebook-ui.subscriptions]

    ;completion
   [pinkgorilla.notebook-ui.completion.component]
   [pinkgorilla.notebook-ui.completion.events]

   ;code-mirror
   [pinkgorilla.notebook-ui.codemirror.codemirror]
   [pinkgorilla.notebook-ui.codemirror.events.core]
   [pinkgorilla.notebook-ui.codemirror.events.completion]

   ; datafy
   [pinkgorilla.notebook-ui.datafy.events-punk]
   [pinkgorilla.notebook-ui.datafy.events-nrepl]
   [pinkgorilla.notebook-ui.datafy.events-dialog]))