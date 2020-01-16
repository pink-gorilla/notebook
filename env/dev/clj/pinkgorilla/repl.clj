(ns pinkgorilla.repl
  (:require
   [clojure.tools.logging :refer (info)]
   ;; [clojure.pprint :refer [pprint]]
   [shadow.cljs.devtools.api :as shadow]
    ;; [shadow.cljs.devtools.server.nrepl :as shadow-nrepl]
   [shadow.cljs.devtools.server :as shadow-server]
    ;; [nrepl.server :as nrepl :refer [start-server stop-server]]
    ;; [cider.nrepl :as cider-nrepl]

   [pinkgorilla.core :as core]
    ;; [pinkgorilla.system :as gsys]
   [pinkgorilla.cli :as cli]))

#_(component/start @system)

#_(defn reload []
    (print "Reload all the things")
    ;; (stop)
    ;; (start)
    )

;; (defonce server (atom nil))

;; (start-figwheel! (config))

;; (ra/print-config)
;; (cljs-repl)


#_(defn stop-server []
    (.stop @server)
    (reset! server nil))

#_(cljs.build.api/build "src"
                        {:main      'hello-world.core
                         :output-to "out/main.js"
                         :verbose   true})

#_(defn start-cljs-repl
    []
    (pback/cljs-repl (cljs.repl.node/repl-env)
                     :watch "in"
                     ;; :reader
                     :output-dir "out"))
#_(defn run-cljs
    []
    (core/run-gorilla-server {})
    (start-cljs-repl))

#_(go
    (cljs.repl/repl (cljs.repl.node/repl-env)
                    ;; :watch "in"
                    :reader
                    :output-dir "out"))

(def cljs-build :app-with-cljs-kernel-dev)
(def gorilla-default-cli-config {:port 9000})
(def gorilla-system (atom nil))

(defn start-system
  ([]
   (start-system gorilla-default-cli-config))
  ([gorilla-config]
   (reset! gorilla-system (core/run-gorilla-server gorilla-config))))

;; TODO: Ugly workaround
(defn -main
  {:shadow/requires-server true}
  [& args]
  (info "Starting with args: " args)
  ;; TODO: Should probably use the one started by shadow-cljs - which works for clj and cljs (connecting with Calva)
  #_(defonce nrepl-server (start-server :port 4001
                                        :handler (apply nrepl/default-handler
                                                        (map resolve (into cider-nrepl/cider-middleware
                                                                           ['shadow.cljs.devtools.server.nrepl/middleware])))))

  ;; options arguments errors summary
  (let [{:keys [options]} (cli/parse-opts args)]
    (start-system options))
  (shadow-server/start!)
  (shadow/watch cljs-build {:verbose true})
  ;; (start "dev")
  )

(comment (+ 1 1)
         (shadow/repl cljs-build))
