(ns pinkgorilla.codemirror.core
  "Glues Parinfer's formatter to a CodeMirror editor"
  (:require
   [clojure.string :as str]
   [taoensso.timbre :as timbre :refer-macros (info)]
   [goog.dom :as gdom]
   [re-frame.core :refer [dispatch]]
   [cljsjs.codemirror]
    ;; ["codemirror"]
   ["parinfer-codemirror"]
   ["codemirror/addon/edit/closebrackets"]
   ["codemirror/addon/edit/matchbrackets"]
   ["codemirror/addon/runmode/runmode"]
   ["codemirror/addon/runmode/colorize"]
   ["codemirror/addon/hint/show-hint"]
   ["codemirror/mode/clojure/clojure"]
   ["codemirror/mode/markdown/markdown"]
    ;; [cljsjs.codemirror.mode.clojure-parinfer]
    ;; [cljsjs.codemirror.mode.xml]
   [pinkgorilla.kernel.core :as kernel] ; send commands to kernel
   [pinkgorilla.codemirror.parinfer-events :refer [add-parinfer-events]]))

(defn configure-cm-globally!
  "Initialize CodeMirror globally"
  []
  (info "Configure Code Mirror globally")                  ;
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


(defn on-mousedown
  [segment-id _ _] ;;  cm event
  (dispatch [:worksheet:segment-clicked segment-id]))

(defn on-change
  "@param {CodeMirror} cm"
  [segment-id cm _] ;; event
  (dispatch [:segment-value-changed segment-id (-> (.getDoc cm) .getValue)]))

(defn on-keydown
  "@param {CodeMirror} cm"
  [cm event]
  (let [keycode (.-which event)]
    (cond
      (and (= keycode 38) (false? (.-shiftKey event)))      ;;up
      (let [cursor (.getCursor cm)
            line (.-line cursor)]
        (when (and (= 0 line) (not (.. cm -state -completionActive)))
          (dispatch [:worksheet:leaveBack])))               ;; :command:worksheet:leaveBack
      (and (= keycode 37) (false? (.-shiftKey event)))      ;; left
      (let [cursor (.getCursor cm)
            line (.-line cursor)
            ch (.-ch cursor)]
        (when (= 0 line ch)
          (dispatch [:worksheet:leaveBack])))               ;; :command:worksheet:leaveBack
      (and (= keycode 40) (false? (.-shiftKey event)))      ;;down
      (let [cursor (.getCursor cm)
            line (.-line cursor)
            linecount (- (.lineCount cm) 1)]
        (when (and (= line linecount) (not (.. cm -state -completionActive)))
          (dispatch [:worksheet:leaveForward])))            ;; :command:worksheet:leaveForward
      (and (= keycode 39) (false? (.-shiftKey event)))      ;; right
      (let [cursor (.getCursor cm)
            line (.-line cursor)
            linecount (- (.lineCount cm) 1)
            ch (.-ch cursor)]
        (when (and (= line linecount) (= ch (.-length (.getLine cm line))))
          (dispatch [:worksheet:leaveForward])))            ;; :command:worksheet:leaveForward
      (and (= keycode 8) (empty? (.getValue (aget cm "doc") #_(.-doc cm)))) ;; delete on empty editor
      (dispatch [:worksheet:deleteBackspace])
      #_:else
      #_(dispatch [:active-segment-value-changed (.getValue (.-doc cm))]))))

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
        cm (when cm-el (.-CodeMirror cm-el))]
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
    (callback completions)))

(defn completer
  "@param {CodeMirror} cm"
  [ns cm callback _]                    ;; options
  (let [cur (.getCursor cm)
        token (.getTokenAt cm cur)
        word (.-string token)
        start (.-start token)
        end (.-end token)
        line (.-line cur)
        doc (.copy (.getDoc cm) false)]
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
         merged-cm-opts (merge (:cm-opts (ctkw cm-default-opts))
                               (:cm-opts override-opts))
         ;; other-opts (:opts override-opts)
         cm (create-regular-editor!
             element
             merged-cm-opts)]
     (.on cm "keydown" on-keydown)
     (.on cm "mousedown" (partial on-mousedown segment-id))
     (when (= "clojure-parinfer" (:mode merged-cm-opts))
       (add-parinfer-events cm))
     (.on cm "change" (partial on-change segment-id))
     cm)))

