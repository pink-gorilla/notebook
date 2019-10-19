(require '[figwheel-sidecar.repl :as r]
         '[figwheel-sidecar.repl-api :as ra :refer [start-figwheel! cljs-repl]]
         '[com.stuartsierra.component :as component]
         '[figwheel-sidecar.system :as sys]
         '[me.lomin.component-restart :as restart]
         '[clojure.core.async :refer [go]]
         ;; '[gorilla-repl.system :as system]
         ;; '[figwheel-sidecar.config :as cfg]
         '[clojure.pprint :refer [pprint]]
         '[gorilla-repl.figwheel :as gfw :refer [config]]
         ;; '[gorilla-repl.core :as gorilla]
         
         ;; awb99:
         ;'[pinkgorilla.ui.gorilla-renderable] ; Renderable Protocol
         ;'[gorilla-middleware.render-values] ; Load this library.
         ;'[pinkgorilla.helper] ; add-dependency
         
         )

;; rlwrap lein run -m clojure.main script/figwheel.clj

(def cfg (config))
;; (pprint cfg)

;; (def system (atom (sys/create-figwheel-system cfg)))
#_(def system (atom (sys/figwheel-system cfg)))

#_(component/start @system)

#_(component/start-system @system)


(start-figwheel! cfg)

(def figwheel-server
  (:figwheel-server @(get-in figwheel-sidecar.repl-api/*repl-api-system* [:figwheel-system :system])))

(defn start []
  (component/start figwheel-server)
  ;; (swap! system component/start)
  )

(defn stop []
  (component/stop figwheel-server)
  ;; (swap! system component/stop)
  )

(defn reload []
  (print "Reload all the things")
  (stop)
  (start))

#_(defn repl []
    (ra/cljs-repl))

;; (let [started (sys/start (system/gorilla-system {:property-file-preferred false}))]
#_(let [started figwheel-sidecar.repl-api/*repl-api-system*]
    (restart/watch (var -main) started))

;; (go ...) Can't set!: *ns* from non-binding thread
;; (restart/watch (var reload) figwheel-server)
;; Caused by: java.io.FileNotFoundException: Could not locate .../script/figwheel__init.class or .../script/figwheel.clj on classpath.
#_(go
  (restart/watch (var reload) figwheel-server))

(cljs-repl)
