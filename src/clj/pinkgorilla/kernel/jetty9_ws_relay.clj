(ns pinkgorilla.kernel.jetty9-ws-relay
  "A websocket handler that passes messages back and forth to an already running nREPL server."
  (:require
   [taoensso.timbre :refer [debug info error]]
    ;; [clojure.tools.logging :refer (debug info warn error)]
   ;[clojure.data.json :as json]
   ;[clojure.walk :as w]
   [clojure.edn :as edn]
   [ring.adapter.jetty9 :as jetty]
   [nrepl.server :as nrepl-server]
   [nrepl.core :as nrepl]
   [nrepl.transport :as transport]
  ; Pinkgorilla Libraries
   [pinkgorilla.middleware.render-values] ; bring into scope
   ))


;; Not as nice as doall, but doall does not work with piped transports / read-timeout (in mem)


(defn- process-replies
  [reply-fn contains-pred replies-seq]
  (loop [s replies-seq]
    (let [msg (first s)]
      (reply-fn msg)
      (if-not (contains-pred (:status msg))
        (recur (rest s))))))

#_(defn- process-message-net
    [& _] ;; channel data
    (throw "fixme")
    #_(let [msg (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)
            client (nrepl/client @gnrepl/conn Long/MAX_VALUE)
            replies-seq (nrepl/message client msg)
            reply-fn (partial process-replies
                              (fn [msg]
                                (server/send!
                                 channel
                                 {:body (json/write-str msg)}))
                              (fn [v] (some #(= "done" %) v)))]
        (reply-fn replies-seq)))

(defn process-message-mem
  "Processes websocket messages"
  [nrepl-handler transport ws timeout data]
  (let [;_ (debug "ws Received: " data)
        msg (assoc (edn/read-string data) :as-html 1)
        [read write] transport
        client (nrepl/client read timeout)
        reply-fn (partial process-replies
                          (fn [msg]
                            (let [payload (pr-str msg)]
                              (info "ws Send " payload)
                              (jetty/send! ws payload)))
                          (fn [s] (contains? s :done)))]
    (reply-fn
     ;; TODO: Not redundant do as clj-kondo claims!
     (do
       (when (:op msg)
         (future (nrepl-server/handle* msg @nrepl-handler write)))
       (client)))))

#_(defn- memory-session
    "Wraps the supplied handler in session middleware that uses a
    private memory store."
    [handler]
    (let [store (mem/memory-store)]
      (session/wrap-session handler {:store store :cookie-name "gorilla-session"})))

#_(defn on-receive-net
    "Relays messages back and forth to an nREPL server. A connection to the nREPL server must have
   previously been made with 'connect-to-nrepl'."
    [_ channel]
    (partial process-message-net channel))

#_(defn on-receive-mem
    "Passes messages into nREPL (in memory)"
    [nrepl-handler session channel]
    (let [transport (or (::transport session)
                        (transport/piped-transports))]
      (partial process-message-mem nrepl-handler transport channel Long/MAX_VALUE)))

(defn ws-processor
  "Creates a websocket thing (not an actual ring-handler).
   Messages are mapped back and forth to EDN.
   Uses https://www.eclipse.org/jetty/javadoc/current/org/eclipse/jetty/websocket/api/WebSocketAdapter.html"
  [nrepl-handler]
  {:on-connect (fn [_] ;; ws
                 (info "ws Connect"))
   :on-error   (fn [_ e]
                 (error "ws Error" e))
   :on-close   (fn [_ws status-code reason]
                 (info "ws Close" status-code reason))
   :on-text    (fn [ws text-message]
                 (debug "ws Rcvd Text" " " text-message)
                 (let [session (:session ws)
                       transport (or (::transport session)
                                     (transport/piped-transports))]
                   (process-message-mem nrepl-handler transport ws Long/MAX_VALUE text-message)))
   :on-bytes   (fn [_ _ _ _]                    ;; ws bytes offset len
                 (info "ws Bytes"))})
