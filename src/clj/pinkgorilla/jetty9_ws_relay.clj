;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

;;; A websocket handler that passes messages back and forth to an already running nREPL server.

(ns pinkgorilla.jetty9-ws-relay
  (:require 
    [nrepl.server :as nrepl-server]
    [nrepl.core :as nrepl]
    [nrepl.transport :as transport]
    [pinkgorilla.nrepl :as gnrepl]
    [pinkgorilla.middleware.render-values]              ;; it's essential this import comes after the previous one!
    ;; [ring.middleware.session :as session]
    ;; [ring.middleware.session.memory :as mem]
    [clojure.data.json :as json]
    [clojure.walk :as w]
    [clojure.tools.logging :refer (debug info warn error)]
    [ring.adapter.jetty9 :as jetty]
    ;; [org.httpkit.server :as server]
    #_[cheshire.core :as json]))


;; Not as nice as doall, but doall does not work with piped transports / read-timeout (in mem)
(defn- process-replies
  [reply-fn contains-pred replies-seq]
  (loop [s replies-seq]
    (let [msg (first s)]
      (reply-fn msg)
      (if-not (contains-pred (:status msg))
        (recur (rest s))))))

(defn- process-message-net
  [channel data]
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

;; (process-message-mem nrepl-handler transport ws timeout data)

(defn- process-message-mem
  ;; [nrepl-handler transport channel timeout data]
  [nrepl-handler transport ws timeout data]
  (let [msg (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)
        [read write] transport
        client (nrepl/client read timeout)
        reply-fn (partial process-replies
                          (fn [msg]
                            (let [payload (json/write-str msg)]
                              (debug "Send " payload)
                              (jetty/send! ws payload)))
                          #_(fn [msg]
                              (server/send!
                                channel
                                {:body    (json/write-str msg)
                                 :session {::tranport transport}}))
                          (fn [s] (contains? s :done)))]
    (debug "Received " data)
    (reply-fn
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


(defn on-receive-net
  "Relays messages back and forth to an nREPL server. A connection to the nREPL server must have
   previously been made with 'connect-to-nrepl'."
  [_ channel]
  (partial process-message-net channel))

(defn on-receive-mem
  "Passes messages into nREPL (in memory)"
  [nrepl-handler session channel]
  (let [transport (or (::transport session)
                      (transport/piped-transports))]
    (partial process-message-mem nrepl-handler transport channel Long/MAX_VALUE)))

#_(defn repl-ring-handler
    "Creates a websocket ring handler for nrepl messages. Messages are mapped back and forth to JSON."
    [on-receive-fn]
    (-> (fn [request]
          (server/with-channel
            request channel
            (server/on-close channel (fn [status]
                                       (warn "FIXME : Channel closed, we should probably close remote connections")))
            (server/on-receive channel (on-receive-fn (:session request) channel))))
        (memory-session)))

(defn ws-processor
  "Creates a websocket thing (not an actual ring-handler). Messages are mapped back and forth to JSON.
  Uses https://www.eclipse.org/jetty/javadoc/current/org/eclipse/jetty/websocket/api/WebSocketAdapter.html"
  [nrepl-handler]
  #_(-> (fn [request]
          (server/with-channel
            request channel
            (server/on-close channel
                             (fn [status]
                               (warn "FIXME : Channel closed, we should probably close remote connections")))
            (server/on-receive channel
                               (on-receive-fn (:session request) channel))))
        (memory-session))
  ;; (process-message-mem nrepl-handler transport ws timeout data)
  {:on-connect (fn [ws]
                 (info "Connect"))
   :on-error   (fn [ws e]
                 (error "Error"))
   :on-close   (fn [ws status-code reason]
                 (info "Close"))
   :on-text    (fn [ws text-message]
                 (debug "Text" " " text-message)
                 (let [session (:session ws)
                       transport (or (::transport session)
                                     (transport/piped-transports))]
                   (process-message-mem nrepl-handler transport ws Long/MAX_VALUE text-message)))
   :on-bytes   (fn [ws bytes offset len]
                 (info "Bytes"))})
