(ns pinkgorilla.kernel.shadowcljs
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [taoensso.timbre :refer-macros (info)]
   [cljs.core.async :refer [<!]]

   [pinkgorilla.kernel.clojurescript :refer [the-eval]]
   [pinkgorilla.kernel.cljs-helper :refer [send-result-eval]]))

(defn init! [config]
  ;(go (<! (cklipse/create-state-eval))s
  (info "Kernel-CLJS init: " config)
  (pinkgorilla.kernel.clojurescript/init! config)
  (info "Kernel-CLJS init: done!")
;  )
  )

;; EVAL

(defn eval!
  [segment-id snippet]
    ;(send-console segment-id "shadow cljs eval started..")
  (go
    (send-result-eval segment-id (<! (the-eval snippet {}))))
  nil)

