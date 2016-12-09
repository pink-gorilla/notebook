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
            [gorilla-middleware.render-values]              ;; it's essential this import comes after the previous one!
            [ring.middleware.session :as session]
            [ring.middleware.session.memory :as mem]
            [clojure.data.json :as json]
            [clojure.walk :as w]
            [clojure.tools.logging :refer (info)]
    #_[cheshire.core :as json])
  #_(:refer clojure.data.json :rename {write-str generate-string,
                                       read-str  parse-string}))

#_(defn- process-replies
    [reply-fn replies-seq]
    (doall (->> replies-seq
                (map reply-fn))))

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
  (let [msg (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)
        client (nrepl/client @gnrepl/conn Long/MAX_VALUE)
        replies-seq (nrepl/message client msg)
        reply-fn (partial process-replies
                          (fn [msg]
                            #_(println "Sending message " msg)
                            (server/send!
                              channel
                              {:body (json/write-str msg)}))
                          (fn [v] (some #(= "done" %) v)))]
    ;; send the messages out over the WS connection one-by-one.
    (reply-fn replies-seq)))

(defn- process-message-mem
  [nrepl-handler transport channel timeout data]
  (let [msg (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)
        [read write] transport
        client (nrepl/client read timeout)
        reply-fn (partial process-replies
                          (fn [msg]
                            #_(print "Sending message " msg)
                            (server/send!
                              channel
                              {:body    (json/write-str msg)
                               :session {::tranport transport}}))
                          (fn [s] (contains? s :done)))]
    (reply-fn
      (do
        (when (:op msg)
          (future (nrepl-server/handle* msg @nrepl-handler write)))
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
  [nrepl-handler request channel]
  (let [session (:session request)
        transport (or (::transport session)
                      (transport/piped-transports))]
    (partial process-message-mem nrepl-handler transport channel Long/MAX_VALUE)))

(defn repl-ring-handler
  "Creates a websocket ring handler for nrepl messages. Messages are mapped back and forth to JSON."
  [on-receive-fn]
  (-> (fn [request]
        (server/with-channel
          request
          channel
          (server/on-receive channel (on-receive-fn request channel))))
      (memory-session)))
