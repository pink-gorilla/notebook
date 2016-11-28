;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

(ns gorilla-repl.core
  (:require [gorilla-repl.handle :as handle]
    ; [gorilla-repl.renderer :as renderer]            ; this is needed to bring the render implementations into scope
            [gorilla-repl.hiccup_renderer]     ; this is needed to bring the render implementations into scope
            [gorilla-repl.version :as version]
            [gorilla-repl.route :as routes]
            [gorilla-repl.system :as sys]
            [gorilla-repl.cli :as cli]
            [clojure.set :as set]
            [clojure.java.io :as io])
  (:gen-class))


(def dev-routes routes/dev-routes)
(def remote-repl-dev-routes routes/remote-repl-dev-routes)

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
                 "gorilla-repl.core/remote-repl-dev-routes"
                 "gorilla-repl.core/dev-routes")
        ;; nrepl-port-file (io/file (or (:nrepl-port-file conf) ".nrepl-port"))

        gorilla-port-file (io/file (or (:gorilla-port-file conf) ".gorilla-port"))
        project (or (:project conf) {})
        keymap (or (:keymap (:gorilla-options conf)) {})
        _ (handle/update-excludes (fn [x] (set/union x (:load-scan-exclude (:gorilla-options conf)))))]
    ;; app startup
    (println "Gorilla-REPL:" version)
    (println "Using project" project)
    ;; build config information for client
    (handle/set-config :project project)
    (handle/set-config :keymap keymap)
    ;; asynchronously check for updates
    (version/check-for-update version)
    (let [s (sys/start {:routes          routes
                        :nrepl-port      nrepl-requested-port
                        :nrepl-host      (:nrepl-host conf)
                        :nrepl-port-file (io/file (or (:nrepl-port-file conf) ".nrepl-port"))
                        :server-port     webapp-requested-port
                        :ip              ip})
          webapp-port (-> s (get-in [:server :httpkit]) .getPort)]
      (spit (doto gorilla-port-file .deleteOnExit) webapp-port)
      (println (str "Running at http://" ip ":" webapp-port "/worksheet.html ."))
      (println "Ctrl+C to exit."))))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args)]
    (println options)
    (run-gorilla-server {:port (:port options)
                         :ip (:host options)
                         :project (:project options)})))
