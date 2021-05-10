(ns pinkgorilla.notebook-ui.schema.schema
  (:require
   [clojure.spec.alpha :as s]))

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

;; settings
(s/def ::md-editor keyword?)
(s/def ::codemirror-theme string?)
(s/def ::code-viewer-theme string?)
(s/def ::code-viewer keyword?)
(s/def ::code-editor keyword?)
(s/def ::layout keyword?)
(s/def ::default-kernel keyword?)
(s/def
  ::settings
  (s/coll-of
   (s/or
    :map (s/keys :req-un [::code-editor
                          ::code-viewer
                          ::code-viewer-theme
                          ::codemirror-theme
                          ::default-kernel
                          ::layout
                          ::md-editor])
    :simple
    keyword?)))



;; explorer


(s/def ::save boolean?)
(s/def ::url string?)
(s/def ::name string?)
(s/def ::repositories
  (s/coll-of (s/keys :req-un [::name ::url] :opt-un [::save])))
(s/def :explorer/config (s/keys :req-un [::repositories]))

(s/def :explorer-repo/filename-canonical string?)
(s/def :explorer-repo/user string?)
(s/def :explorer-repo/repo string?)
(s/def :explorer-repo/storage integer?)
(s/def :explorer-repo/id string?)
(s/def :explorer-repo/root-dir string?)
(s/def :explorer-repo/filename string?)
(s/def :explorer-repo/type keyword?)
(s/def :explorer-repo/index integer?)
(s/def :explorer-repo/edit-date string?)
(s/def :explorer/notebook
  (s/keys
   :req-un
   [:explorer-repo/edit-date
    :explorer-repo/filename
    :explorer-repo/filename-canonical
    :explorer-repo/id
    :explorer-repo/index
    :explorer-repo/meta
    :explorer-repo/repo
    :explorer-repo/root-dir
    :explorer-repo/storage
    :explorer-repo/type
    :explorer-repo/user]))

(s/def :explorer/notebooks
  (s/map-of
   string?
   (s/coll-of :explorer/notebook)))

; keybinding
(s/def ::kb string?)
(s/def ::handler (s/coll-of keyword?))
(s/def ::desc string?)
(s/def ::keybinding (s/keys :req-un [::desc ::handler ::kb]))
(s/def ::all (s/coll-of ::keybinding))
(s/def ::highlight integer?)
(s/def ::visible-items any?)
(s/def ::query string?)
(s/def ::search (s/keys :req-un [::highlight ::visible-items ::query]))
(s/def ::keybindings (s/keys :req-un [::all ::search]))

; notebook
(s/def ::active any?)
(s/def ::tags any?)
(s/def ::meta (s/keys :opt-un [::tags]))
(s/def ::ns any?)
(s/def ::order (s/coll-of any?))
(s/def ::queued (s/coll-of any? :kind set?))
(s/def ::segments (s/coll-of any?))
(s/def ::notebook-hydrated
  (s/keys :req-un [::active
                   ::meta
                   ::ns
                   ::order
                   ::queued
                   ::segments]))
(s/def
  ::documents
  (s/map-of
   integer?
   ::notebook-hydrated))
(s/def ::notebook any?)

(comment
  (s/def :requests integer?)
  (s/def :connected? (s/or :boolean boolean? :integer integer?))
  (s/def :output-ch integer?)
  (s/def :input-ch integer?)
  (s/def :session-id integer?)
  (s/def :nrepl/conn
    (s/keys
     :req-un
     [::connected? ::input-ch ::output-ch ::requests ::session-id]))

  (s/def ::docstring string?)
  (s/def :current/loading boolean?)
  (s/def ::code-editor keyword?)
  (s/def ::show-all boolean?)
  (s/def ::default-kernel keyword?)
  (s/def ::resolve any?)
  (s/def ::info (s/and empty? map?))
  (s/def ::current any?)
  (s/def ::next any?))

(s/def ::db
  (s/keys :req-un [::keybinding
                   ::documents
                   ::notebook])) ; comment



;{:document {:documents {:1 {:segments []}}}}


(s/def ::config map?)

(s/def ::document map?)
(s/def ::segments map?)
(s/def ::notebook nil?)

(s/def ::db
  (s/keys :req-un
                    ;; [::docs ::config ::segments ::segment-order]
          [;::config 
                     ;::worksheet 
           ::document]))