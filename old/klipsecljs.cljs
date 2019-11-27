(ns pinkgorilla.kernel.klipsecljs
  (:require-macros
   [gadjett.core :refer [dbg]]
   [cljs.core.async.macros :refer [go go-loop]])
  (:require


   [pinkgorilla.kernel.cljs-helper :refer [send-result-eval send-console]]
   
   ; klipse
   [klipse-clj.lang.clojure :as cklipse]
   klipse-clj.lang.clojure.bundled-namespaces
   [klipse-clj.repl :refer [get-completions current-alias-map st create-state-compile current-ns-eval current-ns-compile reset-ns-eval! reset-ns-compile!]]
   [klipse-clj.lang.clojure.guard :refer [min-max-eval-duration my-emits watchdog]]
   [klipse-clj.lang.clojure.io :as io]
   [klipse-clj.lang.cljs-repl :refer [error->str]] ;; once error->str is in cljs, take it from there
   [gadjett.core-fn]

   ; clojurescript compiler self hosted
   [cljs.tagged-literals :as tags]
   [cljs.analyzer :as ana]
   [cljs.tools.reader :as r]
   [cljs.tools.reader.reader-types :as rt]
   [cljs.compiler :as compiler]
   [cljs.core.async :refer [timeout chan close! put! <!]]
   [cljs.env :as env]
   [cljs.js :as cljs]

   ; general dependencies we like
   [goog.dom :as gdom]
   [clojure.string :refer [blank?]]
   [clojure.pprint :as pprint]
   [clojure.string :as s]
   [clojure.string :as str]
   [cljs-uuid-utils.core :as uuid]

   [taoensso.timbre :refer-macros (info)]
   
   pinkgorilla.kernel.klipse-bundled-dependencies

   [quil.core :include-macros true]  ; awb99: cannot do this in klipse-bundled-dependencies it seems.
   ))


(defn init! []
  (go (<! (cklipse/create-state-eval))
      (info "klipse init done")))


;; PREPL

;; result is in this form:
#_{:tag :ret
   :ns cljs.user
   :val "14↵"
   :form "(+ 7 7)"}

(defn send-result-prepl [segment-id result]
  (info "cljs eval-prepl result:" result)
  (send-console segment-id (:val result)))

(defn send-eval-message-old!
  [segment-id snippet]
  (do
    (send-console segment-id "cljs eval-prepl started..")
    (go (send-result-prepl segment-id (<! (cklipse/eval-async-prepl snippet {}))))) 0)

;; EVAL



(defn eval!
  [segment-id snippet]
  (do
    (send-console segment-id "cljs eval started..")
    (go (send-result-eval segment-id (<! (cklipse/the-eval snippet {}))))
    nil))
