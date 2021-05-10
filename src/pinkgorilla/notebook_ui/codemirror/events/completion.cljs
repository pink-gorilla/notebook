(ns pinkgorilla.notebook-ui.codemirror.events.completion
  (:require
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.notebook-ui.events.events-segment :refer [codemirror-segment-op]]
   [pinkgorilla.notebook-ui.codemirror.cm-js.completion :refer [cm-current-word cm-replace-current-word]]))

; completions go to cider which uses compliment
; https://github.com/alexander-yakushev/compliment/wiki/Exampleshttps://github.com/alexander-yakushev/compliment/wiki/Examples
; :ns The namespace is which to look for completions (falls back to *ns* if not specified)
; :prefix The prefix for completion candidates
; :context Completion context for compliment.
; :extra-metadata List of extra-metadata fields. Possible values: arglists, doc.

(defn cm-op-complete-get [document segment cm]
  (let [namespace (or (:ns document) "*ns*")
        context ""
        word (cm-current-word cm)]
    (if (empty? word)
      (warn "no current word - cannot get hint!")
      (do
        (info "getting code-completion for ns: " namespace "word: " word)
        (dispatch [:completion/hint word namespace context])))
    segment  ; completion does not change segment 
    ))

#_(reg-event-db
   :codemirror/completion-get
   (fn [db [_]]
     (codemirror-segment-op db cm-op-complete-get)))

#_(reg-event-db
   :codemirror/completion-apply
   (fn [db [_ {:keys [cm]} evt]]
     (let [active (:completion :active)
           word (or (get-in db [:completion :active :candidate]) "xxx")]
       (if active
         (do
           (info "inserting current completion: " word)
           (cm-replace-current-word cm word)
           (.preventDefault evt)
           (assoc-in db [:completion :active] nil))
         (do (info "enter without active completion.")
             db)))))

#_(reg-event-db
   :codemirror-active/completion-apply
   (fn [db [_ {:keys [cm]} evt]]
     (let [cm (get-in db [:codemirror :cm])
           active (get-in db [:completion :active])
           word (or (get-in db [:completion :active :candidate]) "xxx")]
       (when-not cm
         (error ":codemirror-active/completion-apply - no active cm!!"))
       (if active
         (do
           (info "inserting current completion: " word)
           (cm-replace-current-word cm word)
           (.preventDefault evt)
           (assoc-in db [:completion :active] nil))
         (do (warn "enter without active completion.")
             db)))))