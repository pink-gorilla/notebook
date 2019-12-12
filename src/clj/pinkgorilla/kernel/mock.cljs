(ns pinkgorilla.kernel.mock
  #_(:require-macros
     [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
   [taoensso.timbre :refer-macros (info)]
   [re-frame.core :refer [dispatch]]))

(defn init! [_]
  (info "Mock init done"))

(defn send-result [segment-id result]
  (dispatch [:evaluator:console-response segment-id {:console-response result}]))

(defn eval!
  [segment-id _] ;; _content
  (send-result segment-id "Everything is wunderbar!")
  (dispatch [:evaluator:done-response segment-id]))
