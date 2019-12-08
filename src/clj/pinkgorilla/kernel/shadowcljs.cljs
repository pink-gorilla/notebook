(ns pinkgorilla.kernel.shadowcljs
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [taoensso.timbre :refer-macros (info)]
   [cljs.core.async :refer [timeout chan close! put! <!]]

   [pinkgorilla.kernel.clojure :refer [the-eval]]
   [pinkgorilla.kernel.cljs-helper :refer [send-result-eval send-console]]))


(def config
  {:path         "http://localhost:2705/out/gorilla" ; base ur of dependency server / bundle that gets loaded
   ; namespaces that will get loaded into the cljs kernel via our dependency server
   :load-on-init '#{fortune.core
                    awb99.shapes.core
                    quil.core
                    quil.middleware
                    ;quil.sketch
                    ;quil.util
                    reagent.core
                    pinkgorilla.shadow
                    module$node_modules$moment$moment   ; namespace convention of shadow-cljs for npm modules
                    ;ajax.core ; http requests
                   ; pinkgorilla.ui.leaflet
                   ; pinkgorilla.ui.player ; video player
                    pinkgorilla.ui.sparklines ; sparkline charts
                    
                    }})


(defn init! []
  ;(go (<! (cklipse/create-state-eval))
  (pinkgorilla.kernel.clojure/init! config)
  (info "shadow cljs kernel init done")
;  )
  )

;; EVAL

(defn eval!
  [segment-id snippet]
  (do
    (send-console segment-id "shadow cljs eval started..")
    (go (send-result-eval segment-id (<! (the-eval snippet {}))))
    nil))
