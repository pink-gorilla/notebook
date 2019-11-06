(ns pinkgorilla.db
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [cognitect.transit :as t]
   [re-frame.core :refer [dispatch-sync]]
   [cljs.reader]
   ;[pinkgorilla.editor.core :as editor]
   [pinkgorilla.keybindings :refer [all-commands]]
   ))


(def debug?
  ^boolean js/goog.DEBUG)

;; -- Schema -----------------------------------------------------------------

;; This is a clojure.spec specification for the value in app-db. It is like a
;; Schema. See: http://clojure.org/guides/spec
;;
;; The value in app-db should always match this spec. Only event handlers
;; can change the value in app-db so, after each event handler
;; has run, we re-check app-db for correctness (compliance with the Schema).
;;
;; How is this done? Look in events.cljs and you'll notice that all handers
;; have an "after" interceptor which does the spec re-check.
;;
;; None of this is strictly necessary. It could be omitted. But we find it
;; good practice.

(s/def ::config map?)
(s/def ::docs map?)
(s/def ::worksheet map?)
;; (s/def ::segment-order vector?)
#_(s/def ::id int?)
#_(s/def ::title string?)
#_(s/def ::done boolean?)
#_(s/def ::todo (s/keys :req-un [::id ::title ::done]))
#_(s/def ::todos (s/and                                     ;; should use the :kind kw to s/map-of (not supported yet)
                   (s/map-of ::id ::todo)                   ;; in this map, each todo is keyed by its :id
                   #(instance? PersistentTreeMap %)         ;; is a sorted-map (not just a map)
                   ))
#_(s/def ::showing                                          ;; what todos are shown to the user?
    #{:all                                                  ;; all todos are shown
      :active                                               ;; only todos whose :done is false
      :done                                                 ;; only todos whose :done is true
      })
(s/def ::db (s/keys :req-un
                    ;; [::docs ::config ::segments ::segment-order]
                    [::config ::worksheet ::docs]
                    ;; [::todos ::showing]
                    ))




(defn- command-item
  [item]
  (let [kb (:kb item)
        shortcut (or kb "&nbsp;")]
    (merge item {:text (:desc item)
                 :desc (str "<div class= \"command\" >" (:desc item) "</div><div class= \"command-shortcut\" >" shortcut "</div>")})))

(defn ck
  []
  (if (re-matches #".*Win|Linux.*" (.-platform js/navigator))
    "alt"
    "ctrl"))

;; Experimental externs inference
;; https://gist.github.com/swannodette/4fc9ccc13f62c66456daf19c47692799
(defn kb-bind
  [^js/Mousetrap.bindGlobal mousetrap command]
  (if-let [kb (:kb command)]
    (let [kb-val (if (vector? kb) (clj->js kb) kb)
          handler (keyword (:handler command))]
      (.bindGlobal mousetrap kb-val
                   #(dispatch-sync [handler])))))



(defn install-commands
  [command-keymap]
  ;; ** Patch Mousetrap **
  ;;  Install a custom stopCallback so that our keyboard shortcuts work in the codeMirror textareas.
  ;;  This also lets us disable mousetrap processing when we show dialogs
  ;;  (this idea shamelessly stolen from the Mousetrap 'pause' plugin).
  (if-let [mousetrap (if (and (exists? js/window) (.-Mousetrap js/window))
                       (.-Mousetrap js/window))]
    (do
      (set! (.-enabled mousetrap) true)
      (set! (.-enable mousetrap) (fn [x] (set! (.-enabled mousetrap) x)))
      (set! (.-stopCallback mousetrap) #(not (.-enabled mousetrap)))
      (doall (map (partial kb-bind mousetrap) command-keymap)))))


;; -- Initial app-db Value  ---------------------------------------------------
(def initial-db
  {:docs
   {:content  ""
    :position []}
   :all-commands all-commands
   :palette      {;; TODO: We are (ab)using it for files and commands, "inherited" from js version
                  ;; Should probably be two instances
                  :visible-items        nil
                  :show                 false
                  :all-visible-commands (->> all-commands
                                             #_(filter #(:showInMenu %))
                                             (map command-item)
                                             (into []))
                  :all-items            nil
                  :filter               ""
                  :label                ""
                  :highlight            0}
   :worksheet    {}
   :config       {:read-only true}
   :base-path    nil
   :message      nil
   :save         {:show     false
                  :filename nil}
   :settings {:show false }
   })



(defn swap [v i1 i2]
  (assoc v i2 (v i1) i1 (v i2)))
