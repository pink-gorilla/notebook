(ns gorilla-repl.nrepl
  (:require [clojure.tools.nrepl.server :as srv]
            [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [gorilla-repl.sandboxed_interruptible-eval]
            [gorilla-repl.render-values-mw :as render-mw]   ;; it's essential this import comes after the previous one! It
    ;; refers directly to a var in nrepl (as a hack to workaround
    ;; a weakness in nREPL's middleware resolution).
            [clojure.tools.nrepl :as nrepl]
            [cider.nrepl :as cider]))


;; We will open a single connection to the nREPL server for the life of the application. It will be stored here.
;; TODO: Ugly loophole
(def conn (atom nil))

;; Doing it this way with an atom feels wrong, but I can't figure out how to thread an argument into Compojure's
;; routing macro, so I can't pass the connection around, to give a more functional API.
(defn connect-to-nrepl
  "Connect to the nREPL server and store the connection."
  [host port]
  (reset! conn (nrepl/connect :host host :port port)))

(defn- middlewares
  [middlewares sandbox]
  (if sandbox
    (->> middlewares
         (map #(if (= (-> % meta :name name) "interruptible-eval")
                 #'gorilla-repl.sandboxed_interruptible-eval/interruptible-eval
                 %))
         (into []))
    middlewares))

(defn nrepl-handler
  [sandbox cid-mw-vars]
  (let [cider-mw (map resolve cid-mw-vars)
        middleware (conj cider-mw #'render-mw/render-values)]
    (with-redefs [srv/default-middlewares (middlewares srv/default-middlewares sandbox)]
      (apply srv/default-handler middleware))))


(defrecord NReplServer
  [handler server]
  component/Lifecycle
  (start [self]
    (let [nrepl-port (get-in self [:config :config :nrepl-port])
          nrepl-host (get-in self [:config :config :nrepl-host])
          nrepl-port-file (get-in self [:config :config :nrepl-port-file])]
      (if nrepl-port
        (if nrepl-host
          (do
            (log/info "Using nREPL at " nrepl-host ":" nrepl-port)
            (assoc self :remote-connection (connect-to-nrepl nrepl-host nrepl-port)))
          (do
            (log/info "Starting nREPL server on port " nrepl-port)
            (spit (doto nrepl-port-file .deleteOnExit) nrepl-port)
            (assoc self :server (srv/start-server :port nrepl-port :handler (nrepl-handler false cider/cider-middleware)))))
        self)))
  (stop [self]
    (when server
      (srv/stop-server server)
      self)))

(defn new-cider-repl-server
  []
  (map->NReplServer {}))

#_(def nrepl (atom nil))

#_(defn start-and-connect
  ([nrepl-requested-port repl-port-file nrepl-connect-fn]
   (let [nr (srv/start-server :port nrepl-requested-port
                              :handler (nrepl-handler false cider/cider-middleware))
         nrepl-port (:port nr)]
     (println "Started nREPL server on port" nrepl-port)
     (reset! nrepl nr)
     (nrepl-connect-fn "localhost" nrepl-port)
     (spit (doto repl-port-file .deleteOnExit) nrepl-port))))

