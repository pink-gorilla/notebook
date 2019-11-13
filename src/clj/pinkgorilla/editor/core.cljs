(ns pinkgorilla.editor.core
  "Glues Parinfer's formatter to a CodeMirror editor"
  (:require
    [clojure.string :as str :refer [join]]
    [goog.dom :as gdom]
    ;; [dommy.core :as dom :refer-macros [sel sel1 by-id]]
    [cljsjs.codemirror]
    [cljsjs.codemirror.addon.edit.closebrackets]
    [cljsjs.codemirror.addon.edit.matchbrackets]
    [cljsjs.codemirror.addon.runmode.runmode]
    [cljsjs.codemirror.addon.runmode.colorize]
    [cljsjs.codemirror.addon.hint.show-hint]
    [cljsjs.codemirror.mode.clojure]
    [cljsjs.codemirror.mode.clojure-parinfer]
    [cljsjs.codemirror.mode.markdown]
    [cljsjs.codemirror.mode.xml]
    [re-frame.core :refer [dispatch]]
    [pinkgorilla.kernel.core :as kernel]
    [pinkgorilla.editor.editor-support :refer [fix-text!
                                         IEditor
                                         frame-updated?
                                         set-frame-updated!]]
    [taoensso.timbre :as timbre
     :refer-macros (log trace debug info warn error fatal report
                        logf tracef debugf infof warnf errorf fatalf reportf
                        spy get-env log-env)]))



(defn init-cm-globally!
  "Initialize CodeMirror globally"
  []
  (info "Initialize Code Mirror globally")                  ;
  (let [cm-commands (.-commands js/CodeMirror)
        cm-keymap (.-keyMap js/CodeMirror)]
    ;; TODO Quickhack due to missing externs/advanced opt
    (aset cm-commands "doNothing" #())
    (aset cm-keymap "gorilla" #js {:Shift-Enter      "doNothing"
                                   :Shift-Ctrl-Enter "doNothing"
                                   :Shift-Alt-Enter  "doNothing"
                                   :fallthrough      "default"})
    (aset cm-keymap "macDefault" "fallthrough" "basic")
    ;; Need externs, or advanced nukes it
    #_(set! (.-doNothing cm-commands) #())
    #_(set! (.-gorilla cm-keymap) #js {:Shift-Enter      "doNothing"
                                       :Shift-Ctrl-Enter "doNothing"
                                       :Shift-Alt-Enter  "doNothing"
                                       :fallthrough      "default"})
    #_(set! (.-fallthrough (.-macDefault cm-keymap)) "basic")))

;; NOTE:
;; Text is either updated after a change in text or
;; a cursor movement, but not both.
;;
;; When typing, on-change is called, then on-cursor-activity.
;; So we prevent updating the text twice by using an update flag.


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
    (set-frame-updated! cm true)))


(defn parinfer-on-cursor-activity
  "Called after the cursor moves in the editor.
  @param {CodeMirror} cm"
  [cm]
  (when-not (frame-updated? cm)
    (fix-text! cm))
  (set-frame-updated! cm false))

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

(defn on-mousedown
  [segment-id cm event]
  (dispatch [:worksheet:segment-clicked segment-id]))

(defn on-change
  "@param {CodeMirror} cm"
  [segment-id cm event]
  (dispatch [:segment-value-changed segment-id (-> (.getDoc cm) .getValue)]))

(defn on-keydown
  "@param {CodeMirror} cm"
  [cm event]
  (let [keycode (.-which event)]
    (cond
      (and (= keycode 38) (false? (.-shiftKey event)))      ;;up
      (let [cursor (.getCursor cm)
            line (.-line cursor)]
        (if (and (= 0 line) (not (.. cm -state -completionActive)))
          (dispatch [:worksheet:leaveBack])))               ;; :command:worksheet:leaveBack
      (and (= keycode 37) (false? (.-shiftKey event)))      ;; left
      (let [cursor (.getCursor cm)
            line (.-line cursor)
            ch (.-ch cursor)]
        (if (= 0 line ch)
          (dispatch [:worksheet:leaveBack])))               ;; :command:worksheet:leaveBack
      (and (= keycode 40) (false? (.-shiftKey event)))      ;;down
      (let [cursor (.getCursor cm)
            line (.-line cursor)
            linecount (- (.lineCount cm) 1)]
        (if (and (= line linecount) (not (.. cm -state -completionActive)))
          (dispatch [:worksheet:leaveForward])))            ;; :command:worksheet:leaveForward
      (and (= keycode 39) (false? (.-shiftKey event)))      ;; right
      (let [cursor (.getCursor cm)
            line (.-line cursor)
            linecount (- (.lineCount cm) 1)
            ch (.-ch cursor)]
        (if (and (= line linecount) (= ch (.-length (.getLine cm line))))
          (dispatch [:worksheet:leaveForward])))            ;; :command:worksheet:leaveForward
      (and (= keycode 8) (empty? (.getValue (aget cm "doc") #_(.-doc cm)))) ;; delete on empty editor
      (dispatch [:worksheet:deleteBackspace])
      #_:else
      #_(dispatch [:active-segment-value-changed (.getValue (.-doc cm))])
      )))

(def cm-default-opts-common {:lineNumbers       false
                             :matchBrackets     true
                             :autoCloseBrackets "()[]{}\"\""
                             :lineWrapping      true
                             :keyMap            "gorilla"})

#_(aset js/CodeMirror "keyMap" "default" "Shift-Tab" "indentLess")

(def cm-default-opts {:text/x-clojure  {:cm-opts (merge cm-default-opts-common
                                                         {:mode "clojure"})}
                      :text/x-markdown {:cm-opts (merge cm-default-opts-common
                                                         {:mode "text/x-markdown"})}})


;; TODO: Should only fire when we are active!
(defn el-editor
  [el]
  (let [cm-el (gdom/getElementByClass "CodeMirror" el)
        cm (if cm-el (.-CodeMirror cm-el))]
    cm))

(defn do-completions
  "@param {CodeMirror} cm"
  [cur start end callback ns compl]
  (let [ln (.-line cur)
        completions #js {:list (clj->js compl)
                         :from (.Pos js/CodeMirror ln start)
                         :to   (.Pos js/CodeMirror ln end)}]
    (.on js/CodeMirror
         completions
         "select"
         (fn [s]
           (if-not (str/starts-with? s "/")
             (kernel/get-completion-doc :clj s   ;awb99 :clj is a hack
                                       ns
                                       (fn [docs]
                                         (if-not (str/blank? docs)
                                           (dispatch [:show-doc docs])
                                           (dispatch [:hide-doc])))))))
    (.on js/CodeMirror
         completions
         "close"
         #(dispatch [:hide-doc]))
    ;; Show the UI
    (callback completions))
  )

(defn completer [ns cm callback options]
  "@param {CodeMirror} cm"
  (let [cur (.getCursor cm)
        token (.getTokenAt cm cur)
        word (.-string token)
        start (.-start token)
        end (.-end token)
        line (.-line cur)
        doc (.copy (.getDoc cm) false)
        ]
    (.replaceRange doc "__prefix__" #js {:line line :ch start} #js {:line line :ch end})
    ;; TODO: this is a workaround for https://github.com/alexander-yakushev/compliment/issues/15
    ;; Should be fixed, no?
    ;; if (word[0] != "/")
    ;awb99 clj is a hack - dont know where segment var is
    (kernel/get-completions :clj word ns (.getValue doc) (partial do-completions cur start end callback ns))))

(defn complete
  [segment-id ns]
  (let [dom-id (name segment-id)
        cm (-> (gdom/getElement dom-id) el-editor)]         ;; TODO Test bummer
    (.showHint js/CodeMirror cm (partial completer ns) #js {:async true :completeSingle false :alignWithWord false})))

(defn get-token-at-cursor
  [segment-id]
  (let [dom-id (name segment-id)
        cm (-> (gdom/getElement dom-id) el-editor)]         ;; TODO Test bummer
    ;; var token = self.codeMirror.getTokenAt(self.codeMirror.getCursor());
    ;; if (token != null) return token.string;
    (.-string (->> (.getCursor cm)
                   (.getTokenAt cm)))))


;;----------------------------------------------------------------------
;; Setup
;;----------------------------------------------------------------------

(defn create-regular-editor!
  "Create a non-parinfer editor."
  ([element]
   (create-regular-editor! element {}))
  ([element opts]
   (when-not (= "none" (.. element -style -display))
     (let [cm (js/CodeMirror.fromTextArea
                element
                (clj->js opts))]
       cm))))


(defn ^:export create-editor!
  "Create a CodeMirror editor."
  ([element]
   (create-editor! element :opts {}))
  ([element & {:keys [opts content-type segment-id]}]
   (let [ctkw (keyword content-type)
         override-opts (ctkw opts)
         merged-cm-opts (merge (:cm-opts (ctkw cm-default-opts)) (:cm-opts override-opts))
         other-opts (:opts override-opts)
         cm (create-regular-editor!
              element
              merged-cm-opts
              )]
     (.on cm "keydown" on-keydown)
     (.on cm "mousedown" (partial on-mousedown segment-id))
     (if (= "clojure-parinfer" (:mode merged-cm-opts))
       (let [frame-updated (atom false)]
         ;; Extend the code mirror object with some utility methods.
         (specify! cm
           IEditor
           (frame-updated? [this] @frame-updated)
           (set-frame-updated! [this value] (reset! frame-updated value)))
         (.on cm "change" parinfer-on-change)
         (.on cm "beforeChange" parinfer-before-change)
         (.on cm "cursorActivity" parinfer-on-cursor-activity)))
     (.on cm "change" (partial on-change segment-id))
     cm)))

