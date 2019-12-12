(ns pinkgorilla.replikativ
  #_(:require
   ;; [replikativ.peer :refer [server-peer]]
   ;; [replikativ.stage :refer [create-stage! connect!]]

   ;; [kabel.peer :refer [start stop]]
   ;; [konserve.memory :refer [new-mem-store]]
   ;; [konserve.filestore :refer [new-fs-store]]

   ;; [superv.async :refer [<?? S]] ;; core.async error handling
   ;; [clojure.core.async :refer [chan] :as async]
   ;; [taoensso.timbre :as timbre]
     ))

;; (def uri "ws://0.0.0.0:31744")

#_(defn start-replikativ []
    (let [_ (timbre/set-level! :info)
          store (<?? S #_(new-mem-store) (new-fs-store "/tmp/chat42-store"))
          peer (<?? S (server-peer S store uri))
          stage (<?? S (create-stage! "mail:your@email.com" peer))]
      (<?? S (start peer))
    ;; NOTE: you do not need to connect to the test net, but you can :)
    ;(connect! stage "ws://replikativ.io:8888")
      (println "Your chat42 replikativ server peer is up and running! :)" uri)
    ;; HACK blocking main termination
    ;(<?? S (chan))
      ))

#_(comment
    (require '[taoensso.timbre :as timbre])
    (timbre/set-level! :info)

    (stop peer)

    (start-replikativ))
