(ns widget.replikativ
  (:require 
   
   [hasch.core :refer [uuid]]
   [taoensso.timbre :as timbre]
   [superv.async :refer [S] :as sasync]
   [cljs.core.async :refer [>! chan timeout]]
      
   [konserve.memory :refer [new-mem-store]]
   [replikativ.peer :refer [client-peer]]
   [replikativ.stage :refer [create-stage! connect!  subscribe-crdts!]]   
   [replikativ.crdt.ormap.realize :refer [stream-into-identity!]]
   [replikativ.crdt.ormap.stage :as s])
  (:require-macros 
   [superv.async :refer [go-try <? go-loop-try]]
   [cljs.core.async.macros :refer [go-loop]]))


;; 1. app constants
(def user "mail:alice@replikativ.io")
(def ormap-id #uuid "7d274663-9396-4247-910b-409ae35fe98d")
(def uri
  ;; test net
  #_"ws://replikativ.io:8888"
  #_"wss://topiq.es/replikativ/ws"
  ;; alternatively use your own peer :)
  "ws://localhost:31744")


;; Have a look at the replikativ "Get started" tutorial to understand how the
;; replikativ parts work: http://replikativ.io/tut/get-started.html

;; lets transform the OR-MAP operations to the val-atom
(def stream-eval-fns
  {'assoc (fn [S a new]
            (swap! a update-in [:posts] conj new)
            a)
   'dissoc (fn [S a new]
             (swap! a update-in [:posts] disj new)
             a)})


;; this is our main app state
(defonce val-atom (atom {:posts #{}
                         :snackbar {:open false
                                    :message ""}}))


;; standard setup
(defn setup-replikativ []
  (go-try S
          (let [local-store (<? S (new-mem-store))
                local-peer (<? S (client-peer S local-store))
                stage (<? S (create-stage! user local-peer))
                stream (stream-into-identity! stage
                                              [user ormap-id]
                                              stream-eval-fns
                                              val-atom)]
            (<? S (s/create-ormap! stage
                                   :description "messages"
                                   :id ormap-id))
      ;; HACK to display OR-Map creation status message for a moment
            (<? S (timeout 1000))
            (connect! stage uri)
            {:store local-store
             :stage stage
             :stream stream
             :peer local-peer})))

(declare client-state)
;; this is the only state changing function
(defn send-message! [app-state msg]
  (s/assoc! (:stage client-state)
            [user ormap-id]
            (uuid msg)
            [['assoc msg]]))



(defn start-replikativ [& args]
  (go-try S
    (def client-state (<? S (setup-replikativ)))))


