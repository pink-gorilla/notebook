(ns gorilla-repl.jee-interop
  (:use compojure.core)
  (:require                                                 ;; [cheshire.core :as json]
    [clojure.data.json :as json]
    [clojure.walk :as w]
    [clojure.tools.nrepl.server :as nrepl-server]
    [clojure.tools.nrepl :as nrepl]
    [clojure.tools.nrepl [transport :as transport]]
    [gorilla-middleware.cider :as gch]
    [clojure.tools.logging :as log :refer (info)]
    [clojure.pprint :as pp])
  #_(:refer [clojure.data.json :rename {write-str generate-string
                                        read-str  parse-string}]))

(def handler (atom gch/cider-handler))

;; TODO unify all the things!
(defn- process-replies
  [reply-fn replies-seq]
  (log/debug "Process replies")
  (loop [s replies-seq
         result []]
    (let [msg (first s)
          next-result (conj result (reply-fn msg))]
      (if (contains? (:status msg) :done)
        next-result
        (recur (rest s)
               next-result)))))

;; This one is pretty similar to the -mem version of websocket-relay
(defn process-message
  "..."
  [msg store & {:keys [nrepl-handler read-timeout]
                :or   {nrepl-handler (nrepl-server/default-handler)
                       read-timeout  Long/MAX_VALUE}}]
  ;; TODO heartbeat for continuous feeding mode
  (let [[read write :as transport] (or (::transport store)
                                       (do (.put store ::transport (transport/piped-transports))
                                           (::transport store)))
        client (nrepl/client read read-timeout)
        reply-fn (partial process-replies
                          (fn [msg]
                            (json/write-str msg)))]

    (log/debug "Processing message " (with-out-str (pp/pprint msg) " response timeout = " read-timeout))
    (reply-fn
      (do
        (when (:op msg)
          (future (nrepl-server/handle* msg nrepl-handler write)))
        (client)))))

;; Called from java
(defn process-json-message
  [data store]
  (let [m (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)]
    (-> m
        (process-message store :nrepl-handler @handler))))
