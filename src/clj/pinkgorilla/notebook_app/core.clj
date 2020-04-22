(ns pinkgorilla.notebook-app.core
  (:import (java.io PushbackReader))
  (:require
   [clojure.set :as set]
   [clojure.java.io :as io]
   [taoensso.timbre :refer (info)]
  ;Pinkgorilla Libraries
   [pinkgorilla.ui.hiccup_renderer] ; bring the render implementations into scope
   [pinkgorilla.middleware.render-values]

  ;PinkGorilla Notebook
   [pinkgorilla.kernel.jetty9-ws-relay :as ws-relay]
   [pinkgorilla.route :as route]
   [pinkgorilla.explore.explore-handler :refer [update-excludes explore-directories-start]]
   [pinkgorilla.notebook-app.system :as sys]
   [pinkgorilla.notebook-app.cli :as cli]
   [pinkgorilla.notebook.secret] ; bring to scope
   [pinkgorilla.notebook.repl] ; bring to scope
    ;; TODO For notebook compat - fails when called on startup : Could not find a suitable classloader to modify from clojure.lang.LazySeq@532d8051
    ;; #'nrepl.middleware.session/session
   )
  (:gen-class))

#_(defn- load-properties [name type]
    (when-let [resource (resolve-file name type)]
      (load-properties-from-resource resource)))

(defn load-edn [resource]
  (when resource
    (-> resource
        (io/reader)
        (PushbackReader.)
        (read))))


;;found here: https://github.com/metosin/ring-swagger/blob/1c5b8ab7ad7a5735624986bbb6b288aaf168d407/src/ring/swagger/common.clj#L53-L73


(defn deep-merge
  "Recursively merges maps.
   If the first parameter is a keyword it tells the strategy to
   use when merging non-map collections. Options are
   - :replace, the default, the last value is used
   - :into, if the value in every map is a collection they are concatenated
     using into. Thus the type of (first) value is maintained."
  {:arglists '([strategy & values] [values])}
  [& values]
  (let [[values strategy] (if (keyword? (first values))
                            [(rest values) (first values)]
                            [values :replace])]
    (cond
      (every? map? values)
      (apply merge-with (partial deep-merge strategy) values)

      (and (= strategy :into) (every? coll? values))
      (reduce into values)

      :else
      (last values))))

(defn run-gorilla-server
  [conf]
  (info "Got conf " conf)
  (let [version (or (:version conf) "develop")
        webapp-requested-port (or (:port conf) 0)
        ip (or (:ip conf) "127.0.0.1")
        nrepl-requested-port (or (:nrepl-port conf) 0)      ;; auto-select port if none requested
        nrepl-host (:nrepl-host conf)
        routes (if nrepl-host
                 "pinkgorilla.route/remote-repl-handler"
                 "pinkgorilla.route/default-handler")
        default-config (load-edn (io/resource "pink-gorilla.edn"))
        custom-config (if-let [rt-config-file (:runtime-config conf)]
                        (load-edn (io/file rt-config-file))
                        {})
        other-config {:routes          routes
                      :nrepl-port      nrepl-requested-port
                      :nrepl-host      (:nrepl-host conf)
                      :nrepl-port-file (io/file (or (:nrepl-port-file conf) ".nrepl-port"))
                      :server-port     webapp-requested-port
                      ;; TODO bringing the websockets in is a little dirty for now
                      :jetty-options   {:websockets {"/repl" (ws-relay/ws-processor route/nrepl-handler)}}
                      :ip              ip}
        merged-config (deep-merge default-config
                                  custom-config
                                  other-config)
        gorilla-port-file (io/file (or (:gorilla-port-file conf) ".gorilla-port"))
        ;; project (or (:project conf) {})
        ;; keymap (or (:keymap (:gorilla-options conf)) {})
        _ (update-excludes (fn [x] (set/union x (:load-scan-exclude (:gorilla-options conf)))))
        add-deps (get-in merged-config [:settings :additional-runtime-deps])
        _ (info "Got additional deps " add-deps)
        thread (Thread/currentThread)]
    (info "Pink Gorilla Notebook Version" version)
    (->> (clojure.lang.DynamicClassLoader. (.getContextClassLoader thread))
         (.setContextClassLoader thread))
    #_(when-not (empty? add-deps)
        (info "Added additional deps with deps" (helper/add-dependencies add-deps)))
    ;; (println "Using project" project)

    (let [s (sys/start merged-config)
          server (-> s (get-in [:server :jetty]))
          webapp-port (-> server .getConnectors (get 0) .getLocalPort)
          ;; webapp-port (-> s (get-in [:server :httpkit]) .getPort)
          ]
      (spit (doto gorilla-port-file .deleteOnExit) webapp-port)
      (explore-directories-start)
      (info (str "Running at http://" ip ":" webapp-port "/worksheet.html ."))
      s)))

(defn -main
  [& args]
  (let [{:keys [options]} (cli/parse-opts args)]
    (run-gorilla-server options)))
