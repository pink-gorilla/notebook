(ns pinkgorilla.notebook-ui.app.keybindings)

(def keybindings-notebook-app
  [;{:kb "t"       :handler [:settings/show]          :desc "TEST: User Settings Edit"}
   {:kb "alt-g k" :handler [:palette/show]           :desc "Keybindings dialog"}
   ;{:kb "esc"     :handler [:modal/close]            :desc "Dialog Close"} ; for ALL dialogs!
   {:kb "esc"     :handler [:notebook/close-dialog-or-exit-edit] :desc "Dialog Close"} ; for ALL dialogs!

   {:kb "alt-g t" :handler [:reframe10x-toggle] :desc "10x visibility toggle"}

   {:kb "alt-g e" :handler [:bidi/goto :ui/explorer] :desc "Notebook Explorer"}
   {:kb "alt-g u" :handler [:settings/show]          :desc "User Settings Edit"}
   {:kb "alt-g r" :handler [:bidi/goto :ui/nrepl]    :desc "nRepl page"}
   {:kb "alt-g n" :handler [:document/new]           :desc "New notebook"}])

(def keybindings-document
  [{:kb "alt-g s"             :handler [:notebook/save]         :desc "Save Notebook"}
   {:kb "alt-shift-g s"       :handler [:document/save-as]      :desc "Save As"}
   {:kb "alt-g m"             :handler [:notebook/meta-show]    :desc "Edit Notebook Meta Data"}
   #_{:kb "alt-g \\"          :handler [:undo]                  :desc "Undo the last segment operation."}])

(def keybindings-notebook
  [; segment navigation
   {:kb "alt-up"          :handler [:notebook/move :up]      :desc "Move to prior segment"}
   {:kb "alt-down"        :handler [:notebook/move :down]    :desc "Move to next segment"}

   ; segment modify
   {:kb "alt-g l"         :handler [:segment-active/kernel-toggle]  :desc "Language Toggle"}
   {:kb "alt-g i"         :handler [:segment/new-above]      :desc "Create a new segment above active segment"}
   {:kb "alt-g b"         :handler [:segment/new-below]      :desc "Insert a new segment below active segment"}
   {:kb "alt-g x"         :handler [:segment-active/delete]  :desc "Delete the active segment"}
   {:kb "alt-g v"         :handler [:segment-active/toggle-view] :desc "switch markdown/code view in active segment"}
   ; clear
   {:kb "ctrl-backspace"  :handler [:segment-active/clear]  :desc "Clear the output of active segment"}
   {:kb "alt-shift-backspace" :handler [:notebook/clear-all]    :desc "Clear the output of all code segments."}

   ; eval
   {:kb "alt-shift-enter"     :handler [:notebook/evaluate-all] :desc "Evaluate all segments."}
   {:kb "ctrl-shift-enter" :handler [:notebook/evaluate-all]         :desc "Evaluate all segments"}
   {:kb "ctrl-enter" :handler [:segment-active/eval]               :desc "Evaluate the highlighted segment"}

   {:kb "alt-g 1" :handler [:notebook/layout :vertical] :desc "Layout: Vertical"}
   {:kb "alt-g 2" :handler [:notebook/layout :horizontal] :desc "Layout: Horizontal"}
   {:kb "alt-g 3" :handler [:notebook/layout :single] :desc "Layout: Single"}
   {:kb "alt-g 4" :handler [:notebook/layout :stacked] :desc "Layout: Stacked"}])

(def keybindings-completion
  [;{:kb "tab"         :handler [:completion/next]            :desc "move to next auto-completion candidate"}
   {:kb "shift-tab"   :handler [:completion/prior]       :context :codemirror    :desc "move to prior auto-completion candidate"}
   {:kb "ctrl-space"  :handler [:completion/show-all-toggle] :desc "show all / only one line completions"}])

(def keybindings-codemirror
  [; segment navigation - handled in keydown event
   ;{:kb "up"   :handler [:arrow-up]    :desc "Codemirror Key Up"} ; :scope :codemirror
   ;{:kb "down" :handler [:arrow-down] :desc "Codemirror Key Down"} :scope :codemirror
   {:kb "shift-enter" :handler [:codemirror-active/completion-apply]  :desc "applies auto completion"}
   {:kb "ctrl-space"  :handler [:codemirror/completion-get]     ::desc "Show possible auto-completions"}
   #_{:spec "enter"    :handler [:completion/clear]          :key ::clear}
   #_{:spec "ctrl"     :handler [:completion/show-all false] :scope :global :key ::show-all-hide}
   {:kb "alt-g c"     :handler [:clojuredocs]               :desc "Look up the symbol under the cursor in ClojureDocs"}])

(def keybindings-default
  (into []
        (concat
         keybindings-notebook-app
         keybindings-document
         keybindings-notebook
         keybindings-completion
         keybindings-codemirror)))