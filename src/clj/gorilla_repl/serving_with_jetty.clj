(ns gorilla-repl.serving-with-jetty
  (:require [de.otto.tesla.stateful.handler :as handler]
            [compojure.core :as comp]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as c]
            [de.otto.tesla.serving-with-jetty :as tesla-jetty]
            [figwheel.server.jetty-websocket :as fw-jetty]
            [ring.util.response :as resp])
  (:gen-class))


(defrecord JettyServer [config handler]
  c/Lifecycle
  (start [self]
    (log/info "-> starting server")
    (let [handler-404 (comp/ANY "*" _request (resp/status (resp/response "") 404))
          all-handlers (comp/routes (handler/handler handler) handler-404)
          options (tesla-jetty/jetty-options (:config self))
          server (fw-jetty/run-jetty all-handlers (merge {:port  (tesla-jetty/port config)
                                                        :join? false
                                                        ;; TODO: Use this instrumentation once we got websocket
                                                        ;; stuff working
                                                        ;; :configurator tesla-jetty/instrument-jetty
                                                        }
                                                       options))]
      (log/info "options" options)
      (assoc self :jetty server)))
  (stop [self]
    (log/info "<- stopping server")
    (if-let [server (:jetty self)]
      (.stop server))
    self))

(defn new-server [] (map->JettyServer {}))

(defn add-server
  [base-system & server-dependencies]
  (assoc base-system :server (c/using (new-server) (reduce conj [:config :handler] server-dependencies))))
