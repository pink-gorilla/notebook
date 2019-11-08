(require
  '[figwheel.main.api :refer [start]]
  ;; '[com.stuartsierra.component :as component]
  ;; '[me.lomin.component-restart :as restart]
  ;; '[clojure.core.async :refer [go]]gorilla-repl
  ;; '[gorilla-repl.system :as system]
  '[clojure.pprint :refer [pprint]]
  '[pinkgorilla.figwheel :as gfw :refer [main-config]]
  ;; '[pinkgorilla.core :as gorilla]
  )

;; rlwrap lein run -m clojure.main script/figwheel.clj

;; (start "app")
;; (pprint cfg)

;; (def system (atom (sys/create-figwheel-system cfg)))
#_(def system (atom (sys/figwheel-system cfg)))

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

;; (def cfg (main-config))
;; (start cfg)
;; (repl-env "app")
;; (cljs-repl "app")

;; TODO: Does not use :websockets declared figwheel edn
;; (start "dev")

(def cfg (main-config))
(start cfg)
;; (repl-env "app")
;; (cljs-repl "app")
