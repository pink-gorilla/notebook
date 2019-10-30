(require                                                    ;; '[figwheel-sidecar.repl :as r]
  ;; '[figwheel-sidecar.repl-api :as ra :refer [start-figwheel! cljs-repl]]
  '[figwheel.main.api]
  '[figwheel.main.api :refer [start start-join cljs-repl]]
  ;; '[com.stuartsierra.component :as component]
  ;; '[figwheel-sidecar.system :as sys]
  ;; '[me.lomin.component-restart :as restart]
  ;; '[clojure.core.async :refer [go]]
  ;; '[gorilla-repl.system :as system]
  ;; '[figwheel-sidecar.config :as cfg]
  '[clojure.pprint :refer [pprint]]
  '[gorilla-repl.figwheel :as gfw :refer [main-config]]
  ;; '[gorilla-repl.core :as gorilla]
  )

;; rlwrap lein run -m clojure.main script/figwheel.clj

;; (start "app")
;; (pprint cfg)

;; (def system (atom (sys/create-figwheel-system cfg)))
#_(def system (atom (sys/figwheel-system cfg)))

#_(component/start @system)

#_(component/start-system @system)

;; (start-figwheel! cfg)

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

(def cfg (main-config))
(start cfg)
(cljs-repl "app")
