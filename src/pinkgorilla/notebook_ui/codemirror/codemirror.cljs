(ns pinkgorilla.notebook-ui.codemirror.codemirror
  (:require
   [taoensso.timbre :refer-macros [debug debugf info infof warn error]]
   [reagent.core :as r]
   [reagent.dom :as rd]
   [re-frame.core :refer [dispatch]]
   ["codemirror" :as CodeMirror]
   ["codemirror/addon/edit/closebrackets"]
   ["codemirror/addon/edit/matchbrackets"]
   ["codemirror/addon/hint/show-hint"]
   ["codemirror/addon/runmode/runmode"]
   ["codemirror/addon/runmode/colorize"]
   ["codemirror/mode/clojure/clojure"]
   ["codemirror/mode/markdown/markdown"]
   ; [cljsjs.codemirror.mode.xml]
   ; [cljsjs.codemirror.mode.javascript]
   ; ["parinfer-codemirror"]
   ; [cljsjs.codemirror.mode.clojure-parinfer]
   ;["codemirror/keymap/vim"]
   [pinkgorilla.notebook-ui.codemirror.theme]
   [pinkgorilla.notebook-ui.codemirror.highlight]
   [pinkgorilla.notebook-ui.codemirror.cm-events.data :refer [editor-load-content on-change]]
   [pinkgorilla.notebook-ui.codemirror.cm-events.key :refer [on-key-down on-key-up]]
   [pinkgorilla.notebook-ui.codemirror.cm-events.mouse :refer [on-mousedown]]
   [pinkgorilla.notebook-ui.codemirror.options :refer [cm-default-opts cm-keybindings]]
   [pinkgorilla.notebook-ui.editor.data :refer [get-data]]))

(defn configure-cm-globally!
  "Initialize CodeMirror globally"
  []
  (info "Configure Code Mirror globally")                  ;
  (let [cm-commands (.-commands CodeMirror)
        cm-keymap (.-keyMap CodeMirror)]
    (if cm-commands
      (aset cm-commands "doNothing" #())
      (error "could not set codemirror commands!"))
    (if cm-keymap
      (aset cm-keymap "gorilla" (clj->js cm-keybindings))
      (error "could not set codemirror keymap!"))
    nil))

(defn focus-cm!
  [cm]
  (when cm
    (.focus cm)))

(defn blur-cm!
  [cm]
  (when cm
    (let [input (.getInputField cm)]
      (.blur input))))

(defn focus-active-on-edit [id {:keys [segment-active? cm-md-edit?] :as cm-opts} cm]
  (when (and segment-active? cm-md-edit?)
    (debugf "focusing cm %s .." id)
    (focus-cm! cm)
    (dispatch [:codemirror/set-active id cm])))

(defn blur-active-not-edit [id {:keys [segment-active? cm-md-edit?] :as cm-opts} cm]
  (when (and segment-active? (not cm-md-edit?))
    (debugf "blurring cm %s" id)
    (blur-cm! cm)))

(defn destroy-editor [cm-a]
  (if @cm-a
    (do (.toTextArea @cm-a)
        (reset! cm-a nil))
    (warn "Could not kill CodeMirror instance")))

(defn codemirror-reagent
  "code-mirror editor"
  [id cm-opts]
  (let [opts  (merge
               cm-default-opts
               cm-opts)
        cm (atom nil)
        make-event-handler (fn [fun]
                             (fn [s evt]
                               ;(info "cm event - evt: " evt " cm:" s)
                               (fun {:cm-opts opts
                                     :cm @cm
                                     :id id} s evt)))]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (let [el (rd/dom-node this)
              opts-js (clj->js opts)
              ;_ (info "component-did-mount: cm")
              ;cm_ (CodeMirror. el opts-js)
              cm_ (.fromTextArea CodeMirror el opts-js)
              code (get-data :code id)]
          (reset! cm cm_)
          (.setValue cm_ code)

          ; theme - already set in cm constructor
          ;(.setOption inst "theme" (:theme opts))

          (.on cm_ "change" (make-event-handler on-change))
          (.on cm_ "keydown"  (make-event-handler on-key-down))
          (.on cm_ "keyup"   (make-event-handler on-key-up))
          (.on cm_ "mousedown" (make-event-handler on-mousedown))

          ;(blur-active-not-edit (:id eval-result) opts cm_)
          ;(focus-active-on-edit (:id eval-result) opts cm_)

          ;(when on-cm-init (on-cm-init inst))
          ))

      :component-will-unmount
      (fn [this]
        (debug "cm component-will-unmount")
        (destroy-editor cm))

      :component-did-update
      (fn [this old-argv]
        (let [[_ id opts] (r/argv this)]
          ;(info "component-did-update: current buffer: " eval-result9         
          (editor-load-content @cm (get-data :code id))
          ;(blur-active-not-edit (:id eval-result) opts @cm)
          ;(focus-active-on-edit (:id eval-result) opts @cm)
          ;
          ))

      :reagent-render
      (fn []
        (let [{:keys [readOnly]} opts]
          ;(info "read-only?: " readOnly)
          (if readOnly
            [:textarea {:read-only true}]
            [:textarea])))})))



