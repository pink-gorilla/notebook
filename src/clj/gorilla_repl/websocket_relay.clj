;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

;;; A websocket handler that passes messages back and forth to an already running nREPL server.

(ns gorilla-repl.websocket-relay
  (:require [org.httpkit.server :as server]
            [clojure.tools.nrepl.server :as nrepl-server]
            [clojure.tools.nrepl :as nrepl]
            [clojure.tools.nrepl [transport :as transport]]
            [gorilla-repl.nrepl :as gnrepl]
            [gorilla-middleware.middleware :as gmw]
            [gorilla-middleware.render-values]   ;; it's essential this import comes after the previous one!
            [cider.nrepl]
            [ring.middleware.session :as session]
            [ring.middleware.session.memory :as mem]
            [clojure.data.json :as json]
            [clojure.walk :as w]
            #_[cheshire.core :as json])
  #_(:refer clojure.data.json :rename {write-str generate-string,
                                      read-str parse-string}))

(defn- process-replies
  [reply-fn replies]
  (doall (->> replies
              (map reply-fn))))

(defn- process-message-net
  [channel data]
  (let [parsed-message (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)
        client (nrepl/client @gnrepl/conn Long/MAX_VALUE)
        replies (nrepl/message client parsed-message)]
    ;; send the messages out over the WS connection one-by-one.
    (let [reply-fn (partial process-replies
                            #(server/send!
                              channel
                              {:body (json/write-str %)}))]
      (reply-fn replies))))

(defn- process-message-mem
  [transport channel timeout data]
  (let [msg (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)
        [read write] transport
        client (nrepl/client read timeout)]
    ((partial process-replies #(server/send!
                                channel
                                {:body    (json/write-str %)
                                 :session {::tranport transport}}))
      (do
        (when (:op msg)
          (future (nrepl-server/handle* msg @gmw/gorilla-handler write)))
        (client)))))

(defn- memory-session
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
  [request channel]
  (let [session (:session request)
        transport (or (::transport session)
                      (transport/piped-transports))]
    (partial process-message-mem transport channel 1000)))

(defn repl-ring-handler
  "Creates a websocket ring handler for nrepl messages. Messages are mapped back and forth to JSON."
  [on-receive-fn]
  (-> (fn [request]
        (server/with-channel
          request
          channel
          (server/on-receive channel (on-receive-fn request channel))))
      (memory-session)))
