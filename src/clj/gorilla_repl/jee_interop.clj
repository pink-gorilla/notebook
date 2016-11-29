(ns gorilla-repl.jee-interop
  (:use compojure.core)
  (:require ;; [cheshire.core :as json]
            [clojure.data.json :as json]
            [clojure.walk :as w]
            [clojure.tools.nrepl.server :as nrepl-server]
            [clojure.tools.nrepl :as nrepl]
            [clojure.tools.nrepl [transport :as transport]]
            [gorilla-middleware.cider :as gch]
            [clojure.tools.logging :as log]
            [cider.nrepl :as cider]
            [clojure.pprint :as pp])
  #_(:refer [clojure.data.json :rename {write-str generate-string
                                      read-str parse-string}]))

;; the combined routes - we serve up everything in the "public" directory of resources under "/".
;; The REPL traffic is handled in the websocket-transport ns.
#_(defroutes app-routes
           (GET "/alfresco/lambdalf/foo" [] "Hello Alfresco Foo")
           (GET "/alfresco/lambdalf/gorilla-repl/load" [] (wrap-api-handler load-worksheet))
           (POST "/alfresco/lambdalf/gorilla-repl/save" [] (wrap-api-handler save))
           (GET "/alfresco/lambdalf/gorilla-repl/gorilla-files" [] (wrap-api-handler gorilla-files))
           (GET "/alfresco/lambdalf/gorilla-repl/config" [] (wrap-api-handler config))
           ;; (GET "/repl" [] ws-relay/ring-handler)
           (route/resources "/alfresco/lambdalf/gorilla-repl/")
           (route/files "/alfresco/lambdalf/gorilla-repl/project-files" [:root "."]))

#_(defn get-routes [] app-routes)

#_(defn drawbridge-ring-handler
         "Returns a Ring handler implementing an HTTP transport endpoint for nREPL.
          The handler will work when routed onto any URI.  Note that this handler
          requires the following standard Ring middleware to function properly:
            * keyword-params
            * nested-params
            * wrap-params
          a.k.a. the Compojure \"api\" stack.
          nREPL messages should be encoded into POST request parameters; messages
          are only accepted from POST parameters.
          A GET or POST request will respond with any nREPL response messages cached
          since the last request.  If:
            * the handler is created with a non-zero :default-read-timeout, or
            * a session's first request to the handler specifies a non-zero
              timeout via a REPL-Response-Timeout header
          ...then each request will wait the specified number of milliseconds for
          additional nREPL responses before finalizing the response.
          All response bodies have an application/json Content-Type, consisting of
          a map in the case of an error, or an array of nREPL response messages
          otherwise.  These messages are output one per line (/ht CouchDB), like so:
          [
          {\"ns\":\"user\",\"value\":\"3\",\"session\":\"d525e5..\"}
          {\"status\":[\"done\"],\"session\":\"d525e5..\"}
          ]
          A custom nREPL handler may be specified when creating the handler via
          :nrepl-handler.  The default
          (via `(clojure.tools.nrepl.nrepl-server/default-handler)`) is appropriate
          for textual REPL interactions, and includes support for interruptable
          evaluation, sessions, readably-printed evaluation values, and
          prompting for *in* input.  Please refer to the main nREPL documentation
          for details on semantics and message schemas for these middlewares."
         [& {:keys [nrepl-handler default-read-timeout cookie-name]
             :or   {nrepl-handler        (nrepl-server/default-handler)
                    default-read-timeout 0
                    cookie-name          "drawbridge-session"}}]
         ;; TODO heartbeat for continuous feeding mode
         (-> (fn [{:keys [params session headers request-method] :as request}]
               ;(println params session)
               (let [msg (clojure.walk/keywordize-keys params)]
                 (cond
                   (not (#{:post :get} request-method)) illegal-method-error

                   (and (:op msg) (not= :post request-method)) message-post-error

                   :else
                   (let [[read write :as transport] (or (::transport session)
                                                        (transport/piped-transports))
                         client (or (::client session)
                                    (nrepl/client read (if-let [timeout (get headers response-timeout-header*)]
                                                         (Long/parseLong timeout)
                                                         default-read-timeout)))]
                     (response transport client
                               (do
                                 (when (:op msg)
                                   (future (nrepl-server/handle* msg nrepl-handler write)))
                                 (client)))))))
             (memory-session :cookie-name cookie-name)))

(def handler (atom gch/cider-handler))

(defn- response
  [response-seq]
  (doall (map json/write-str response-seq)))

#_(def ^:private nrepl-handler (apply nrepl-server/default-handler
                                    (-> (map resolve cider/cider-middleware)
                                        (conj #'render-mw/render-values))))

(defn process-message
  "..."
  [msg store & {:keys [nrepl-handler read-timeout]
                :or   {nrepl-handler (nrepl-server/default-handler)
                       read-timeout  1000}}]                ;; Long/MAX_VALUE -> Not what we want
  ;; TODO heartbeat for continuous feeding mode
  (log/info "Got handler" nrepl-handler)
  (let [[read write :as transport] (or (::transport store)
                                       (do (.put store ::transport (transport/piped-transports))
                                           (::transport store)))
        client (nrepl/client read read-timeout)]
    (log/debug "Processing message " (with-out-str (pp/pprint msg) " response timeout = " read-timeout))
    (response (do
                (when (:op msg)
                  (future (nrepl-server/handle* msg nrepl-handler write)))
                (client)))))

;; Called from java
(defn process-json-message
  [data store]
  (let [m (assoc (-> (json/read-str data) w/keywordize-keys) :as-html 1)]
    (-> m
        (process-message store :nrepl-handler @handler))))
