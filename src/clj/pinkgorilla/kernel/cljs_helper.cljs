(ns pinkgorilla.kernel.cljs-helper
  #_(:require-macros
     [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.ui.gorilla-renderable :refer [#_render render-renderable-meta]]
   [pinkgorilla.ui.rendererCLJS]))


; dispatch results to reframe


(defn send-console [segment-id result]
  (dispatch
   [:evaluator:console-response
    segment-id
    {:console-response result}]))

(defn send-value
  "display the eval result in the notebook"
  ([segment-id result]
   (send-value segment-id result 'cljs.user))
  ([segment-id result namespace]
   (dispatch
    [:evaluator:value-response
     segment-id
     result
     namespace])))

(defn send-error [segment-id error-text]
  (dispatch
   [:evaluator:error-response
    {:error-text error-text
     :segment-id segment-id}]))

; render result from repl to intermediary format used in notebook ui

(defn render-embedded
  "a simple render implementation for testing
   this is a quick replacement for either: render render-renderable-meta"
  [result]
  (let [s (pr-str result)]
    {:value-response
     {:type "html"
      :content ["span" s]
      :value s}}))


;; result:
;; [:ok value]
;; [:error #error {:message "ERROR", :data {:tag :cljs/analysis-error}, :cause #object[TypeError TypeError: bongo.trott.g is undefined]}]


(defn send-result-eval [segment-id result]
  (let [[type data] result]
    (info "cljs eval result:" result)
    ;(info "cljs eval result meta:" (meta data))
    ;(send-console segment-id (str " type: " (type data) "data: " (pr-str data)))
    (case type
      :ok  (send-value segment-id (render-renderable-meta data))
      :error (send-error segment-id (pr-str data))
      (info "cljs kernel received unknown result type: " type "data: " data))
    (dispatch [:evaluator:done-response segment-id]))) ; assumption: only one response to eval