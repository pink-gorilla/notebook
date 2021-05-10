(ns pinkgorilla.notebook-ui.codemirror.extension.parinfrarize
  (:require [clojure.string :refer [join]]
            [parinfer-codemirror.state :refer [state
                                               empty-editor-state]]
            [parinfer-codemirror.editor-support :refer [update-cursor!
                                                        fix-text!
                                                        cm-key
                                                        IEditor
                                                        get-prev-state
                                                        frame-updated?
                                                        set-frame-updated!]]))

;; stolen from reepl 
;; not yet integrated

;;----------------------------------------------------------------------
;; Life Cycle events
;;----------------------------------------------------------------------

;; NOTE:
;; Text is either updated after a change in text or
;; a cursor movement, but not both.
;;
;; When typing, on-change is called, then on-cursor-activity.
;; So we prevent updating the text twice by using an update flag.

(def frame-updates (atom {}))

(defn before-change
  "Called before any change is applied to the editor."
  [cm change]
  ;; keep CodeMirror from reacting to a change from "setValue"
  ;; if it is not a new value.
  (when (and (= "setValue" (.-origin change))
             (= (.getValue cm) (join "\n" (.-text change))))
    (.cancel change)))

(defn on-change
  "Called after any change is applied to the editor."
  [cm change]
  (when (not= "setValue" (.-origin change))
    (fix-text! cm :change change)
    (update-cursor! cm change)
    (set-frame-updated! cm true)))

(defn on-cursor-activity
  "Called after the cursor moves in the editor."
  [cm]
  (when-not (frame-updated? cm)
    (fix-text! cm))
  (set-frame-updated! cm false))

(defn on-tab
  "Indent selection or insert two spaces when tab is pressed.
  from: https://github.com/codemirror/CodeMirror/issues/988#issuecomment-14921785"
  [cm]
  (if (.somethingSelected cm)
    (.indentSelection cm)
    (let [n (.getOption cm "indentUnit")
          spaces (apply str (repeat n " "))]
      (.replaceSelection cm spaces))))

;;----------------------------------------------------------------------
;; Setup
;;----------------------------------------------------------------------

(def editor-opts
  {:mode "clojure-parinfer"
   :matchBrackets true
   :extraKeys {:Tab on-tab}})

(aset js/CodeMirror "keyMap" "default" "Shift-Tab" "indentLess")

(defn parinferize!
  "Add parinfer goodness to a codemirror editor"
  ([cm key- parinfer-mode]
   (when-not (get @state key-)
     (let [initial-state (assoc empty-editor-state
                                :mode parinfer-mode)
           prev-editor-state (atom nil)]

       ;; (set! (.-id wrapper) (str "cm-" element-id))

       (when-not (get @state key-)
         (swap! frame-updates assoc key- {}))

       (swap! state update-in [key-]
              #(-> (or % initial-state)
                   (assoc :cm cm)))

       ;; Extend the code mirror object with some utility methods.
       (specify! cm
                 IEditor
                 (get-prev-state [this] prev-editor-state)
                 (cm-key [this] key-)
                 (frame-updated? [this] (get-in @frame-updates [key- :frame-updated?]))
                 (set-frame-updated! [this value] (swap! frame-updates assoc-in [key- :frame-updated?] value)))

       ;; handle code mirror events
       (.on cm "change" on-change)
       (.on cm "beforeChange" before-change)
       (.on cm "cursorActivity" on-cursor-activity)

       cm))))

;;----------------------------------------------------------------------
;; Setup
;;----------------------------------------------------------------------

(defn on-state-change
  "Called everytime the state changes to sync the code editor."
  [_ _ old-state new-state]
  (doseq [[k {:keys [cm text]}] new-state]
    (let [changed? (not= text (.getValue cm))]
      (when changed?
        (.setValue cm text)))))

(defn force-editor-sync! []
  (doseq [[k {:keys [cm text]}] @state]
    (.setValue cm text)))

(defn start-editor-sync! []
  ;; sync state changes to the editor
  (add-watch state :editor-updater on-state-change)
  (force-editor-sync!))