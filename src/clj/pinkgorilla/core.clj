(ns pinkgorilla.core
  (:require
   [clojure.set :as set]
   [clojure.java.io :as io]
   [pinkgorilla.handle :as handle]
   [pinkgorilla.jetty9-ws-relay :as ws-relay]
   [pinkgorilla.route :as route]
   [pinkgorilla.ui.hiccup_renderer] ; this is needed to bring the render implementations into scope
   [pinkgorilla.middleware.render-values]
     ;[pinkgorilla.ui.gorilla-renderable]
   [pinkgorilla.storage.explore-handler :refer [update-excludes]]

   [pinkgorilla.system :as sys]
   [pinkgorilla.cli :as cli])
  (:gen-class))



(def gorilla-system (atom nil))

;; (keys @gorilla-system)
;; (get-in-system [:config :config])
(defn get-in-system [path]
  (get-in @gorilla-system path))

;; TODO WIP, we can to better
;; lein plugin entry point
(defn run-gorilla-server
  [conf]
  (println "Got conf " conf)
  ; get configuration information from parameters
  (let [version (or (:version conf) "develop")
        webapp-requested-port (or (:port conf) 0)
        ip (or (:ip conf) "127.0.0.1")
        nrepl-requested-port (or (:nrepl-port conf) 0)      ;; auto-select port if none requested
        nrepl-host (:nrepl-host conf)
        routes (if nrepl-host
                 "pinkgorilla.route/remote-repl-handler"
                 "pinkgorilla.route/default-handler")
        gorilla-port-file (io/file (or (:gorilla-port-file conf) ".gorilla-port"))
        project (or (:project conf) {})
        keymap (or (:keymap (:gorilla-options conf)) {})
        _ (update-excludes (fn [x] (set/union x (:load-scan-exclude (:gorilla-options conf)))))]
    ;; app startup
    (println "Gorilla-REPL:" version)
    (println "Using project" project)
    ;; build config information for client
    (handle/set-config :project project)
    (handle/set-config :keymap keymap)
    ;; asynchronously check for updates
    ;; (version/check-for-update version)
    (let [s (sys/start {:routes          routes
                        :nrepl-port      nrepl-requested-port
                        :nrepl-host      (:nrepl-host conf)
                        :nrepl-port-file (io/file (or (:nrepl-port-file conf) ".nrepl-port"))
                        :server-port     webapp-requested-port
                        ;; TODO bringing the websockets in is a little dirty for now
                        :jetty-options   {:websockets {"/repl" (ws-relay/ws-processor route/nrepl-handler)}}
                        :ip              ip})
          server (-> s (get-in [:server :jetty]))
          webapp-port (-> server .getConnectors (get 0) .getLocalPort)
          ;; webapp-port (-> s (get-in [:server :httpkit]) .getPort)
          ]
      (spit (doto gorilla-port-file .deleteOnExit) webapp-port)
      (println (str "Running at http://" ip ":" webapp-port "/worksheet.html ."))
      s)))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args)]
    (reset! gorilla-system
            (run-gorilla-server options))))
