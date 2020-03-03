(ns pinkgorilla.codemirror.parinfer-events
  "Glues Parinfer's formatter to a CodeMirror editor"
  (:require
   [clojure.string :as str :refer [join]]
   [pinkgorilla.codemirror.editor-support :as support :refer [fix-text!
                                                              IEditor
                                              ;frame-updated?
                                              ;set-frame-updated!
                                                              ]]))

(defn parinfer-before-change
  "Called before any change is applied to the editor.
  @param {CodeMirror} cm"
  [cm change]
  ;; keep CodeMirror from reacting to a change from "setValue"
  ;; if it is not a new value.
  (when (and (= "setValue" (.-origin change))
             (= (.getValue cm) (join "\n" (.-text change))))
    (.cancel change)))

(defn parinfer-on-change
  "Called after any change is applied to the editor.
  @param {CodeMirror} cm"
  [cm change]
  (when (not= "setValue" (.-origin change))
    (fix-text! cm :change change)
    (support/set-frame-updated! cm true)))

(defn parinfer-on-cursor-activity
  "Called after the cursor moves in the editor.
  @param {CodeMirror} cm"
  [cm]
  (when-not (support/frame-updated? cm)
    (fix-text! cm))
  (support/set-frame-updated! cm false))

(defn parinfer-on-tab
  "Indent selection or insert two spaces when tab is pressed.
  from: https://github.com/codemirror/CodeMirror/issues/988#issuecomment-14921785
  @param {CodeMirror} cm"
  [cm]
  (if (.somethingSelected cm)
    (.indentSelection cm)
    (let [n (.getOption cm "indentUnit")
          spaces (apply str (repeat n " "))]
      (.replaceSelection cm spaces))))

(defn add-parinfer-events [cm]
  (let [atom-frame-updated (atom false)
             ;cm2 (reify
             ;      IEditor
             ;      (frame-updated? [_ #_this] @atom-frame-updated)
             ;      (set-frame-updated! [_ #_this value] (reset! atom-frame-updated value)))
        ]
         ;; Extend the code mirror object with some utility methods.
         ;          (reify         
    (specify! cm
              IEditor
              (frame-updated? [_ #_this] @atom-frame-updated)
              (set-frame-updated! [_ #_this value] (reset! atom-frame-updated value)))
    (.on cm "change" parinfer-on-change)
    (.on cm "beforeChange" parinfer-before-change)
    (.on cm "cursorActivity" parinfer-on-cursor-activity)))