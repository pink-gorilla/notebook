(ns pinkgorilla.repl
  (:require
    ;; '[com.stuartsierra.component :as component]
    ;; '[me.lomin.component-restart :as restart]
    [nrepl.server :refer [start-server stop-server]]
    ;; [cljs.build.api]
    ;; [cljs.repl]
    ;; [cljs.repl.node]
    ;; [clojure.core.async :refer [go]]
    [pinkgorilla.core :as core]
    [figwheel.main.api :refer [start start-join cljs-repl]]
    [clojure.pprint :refer [pprint]]
    ;; [pinkgorilla.figwheel :as gfw :refer [main-config]]
    ;; [pinkgorilla.system :as gsys]
    ))

;; (def system (atom (sys/create-figwheel-system cfg)))
;; (def system (atom (sys/figwheel-system cfg)))

#_(component/start @system)

#_(component/start-system @system)

;; (start-figwheel! cfg)
;(pinkgorilla.replikativ/start-replikativ)

#_(def figwheel-server
    (:figwheel-server @(get-in figwheel-sidecar.repl-api/*repl-api-system* [:figwheel-system :system])))

#_(defn start-figwheel-component []
    ;; (component/start figwheel-server)
    ;; (swap! system component/start)
    )

#_(defn stop-figwheel-component []
    ;; (component/stop figwheel-server)
    ;; (swap! system component/stop)
    )

#_(defn reload []
    (print "Reload all the things")
    ;; (stop)
    ;; (start)
    )

;; (defonce server (atom nil))

;; (start-figwheel! (config))

;; (ra/print-config)
;; (cljs-repl)

#_(defn start-server
    "used for starting the server in development mode from REPL"
    [& [port]]
    (let [port (if port (Integer/parseInt port) 3000)]
      (reset! server
              (serve (get-handler)
                     {:port         port
                      :auto-reload? true
                      :join?        false}))
      (println (str "You can view the site at http://localhost:" port))))

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



(def gorilla-default-cli-config {:port 9000})

(def gorilla-system (atom nil))

(defn start-system
  ([]
   (start-system gorilla-default-cli-config))
  ([gorilla-config]
   (reset! gorilla-system (core/run-gorilla-server gorilla-config))))

;; TODO: Ugly workaround
(defn -main [& args]
  (defonce server (start-server :port 4001))
  (start-system gorilla-default-cli-config)
  (start "dev"))
