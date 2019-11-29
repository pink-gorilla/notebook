(ns pinkgorilla.kernel.mock
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
   [clojure.string :as str]
   [taoensso.timbre :refer-macros (info)]
   [cljs-uuid-utils.core :as uuid]
   [re-frame.core :refer [dispatch]]))

(defn init! []
  (info "mock init done"))

(defn send-result [segment-id result]
  (dispatch [:evaluator:console-response segment-id {:console-response result}]))

(defn eval!
  [segment-id content]
  (send-result segment-id "Everything is wunderbar!"))
