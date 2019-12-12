(ns pinkgorilla.db
  (:require
   [clojure.spec.alpha :as s]
  ; [cognitect.transit :as t]
  ; [re-frame.core :refer [dispatch-sync]]
   [cljs.reader]
    ;; [pinkgorilla.keybindings :refer [visible-commands]]
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
(s/def ::navbar-menu-active? boolean?)
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

(defn ck
  []
  (if (re-matches #".*Win|Linux.*" (.-platform js/navigator))
    "alt"
    "ctrl"))

; explore:
(def form-default {:data    {}
                   :errors  {}
                   :state   :sleeping})

;; -- Initial app-db Value  ---------------------------------------------------
(def initial-db
  {:config       {:read-only true}
   :base-path    nil

   :docs {:content  ""
          :position []}

   ; navbar, and main page navigation
   :navbar-menu-active? true
   :current-view :home
   :main :notebook ; integrate this to the way navbar works.
   :nav {}  ; todo: remove this - came form notebook explorer from open source clojure

   ; old command palette
   :all-commands []
   :palette      {;; TODO: We are (ab)using it for files and commands, "inherited" from js version
                  ;; Should probably be two instances
                  :visible-items        nil
                  :show                 false
                  :all-visible-commands [];; visible-commands
                  :all-items            nil
                  :filter               ""
                  :label                ""
                  :highlight            0}

   ; notifications
   :notifications []
   :message      nil  ; TODO: remove message, after notification system works 100% ok

   ; dialogs
   :dialog {:settings false
            :save false
            :meta false}

   ; notebook editor
   :worksheet    {:meta {}}
   :settings     {:default-kernel :clj
                  :editor :text
                  :github-token ""}

   :storage nil
   :storage-load-error nil

   :kernel-clj {:connected false
                :session-id nil}

   ; explore:
   :projects     {:selected nil}
   :forms        {:projects {:create form-default
                             :update form-default
                             :search form-default}}
   :data         {:projects []}
   :initialized true

   :dev {:reframe10x-visible? false}})




