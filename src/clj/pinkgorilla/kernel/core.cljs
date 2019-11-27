(ns pinkgorilla.kernel.core
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [pinkgorilla.prefs :as prefs]

   [pinkgorilla.kernel.nrepl :as nrepl]
   [pinkgorilla.kernel.mock :as mock]))

(prefs/if-cljs-kernel
 ;(require '[pinkgorilla.kernel.klipsecljs :as cljs-kernel])
 (require '[pinkgorilla.kernel.shadowcljs :as cljs-kernel])
 (require '[pinkgorilla.kernel.mock :as cljs-kernel]))


(defn eval! [kernel segment-id snippet]
  (case kernel
    :clj (nrepl/eval! segment-id snippet)
    :mock (mock/eval! segment-id snippet)
    :cljs (cljs-kernel/eval! segment-id snippet)
    (info "cannot eval - unknown kernel!")))


(defn get-completion-doc [kernel symbol ns callback]
  (case kernel
    :clj (nrepl/get-completion-doc symbol ns callback)
    (info "get-completion-doc not implemented for kernel: " kernel)))

(defn get-completions [kernel symbol ns context callback]
  (case kernel
    :clj (nrepl/get-completions symbol ns context callback)
    (info "get-completion-doc not implemented for kernel: " kernel)))

(defn resolve-symbol [kernel symbol ns callback]
   (case kernel
    :clj (nrepl/resolve-symbol symbol ns callback)
    (info "get-completion-doc not implemented for kernel: " kernel)))