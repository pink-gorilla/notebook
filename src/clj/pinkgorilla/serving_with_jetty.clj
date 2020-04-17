(ns pinkgorilla.serving-with-jetty
  (:require
   [de.otto.tesla.stateful.handler :as handler]
   [compojure.core :as comp]
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as c]
   [de.otto.tesla.serving-with-jetty :as tesla-jetty]
   [ring.adapter.jetty9 :as jetty-ws]
   [ring.util.response :as resp])
  (:gen-class))

(defrecord JettyServer [config handler]
  c/Lifecycle
  (start [self]
    (log/info "-> Starting server")
    (let [handler-404 (comp/ANY "*" _request (resp/status (resp/response "") 404))
          all-handlers (comp/routes (handler/handler handler) handler-404)
          options (tesla-jetty/jetty-options (:config self))
          server (jetty-ws/run-jetty all-handlers (merge {:port (tesla-jetty/port config)
                                                          :join?  false
                                                        ;; TODO: Use this instrumentation once we got websocket
                                                        ;; stuff working
                                                        ;; :configurator tesla-jetty/instrument-jetty
                                                          }
                                                         options))]
      (log/info "Options" options)
      (assoc self :jetty server)))
  (stop [self]
    (log/info "<- Stopping server")
    (if-let [server (:jetty self)]
      (.stop server))
    self))

(defn new-server [] (map->JettyServer {}))

(defn add-jetty-server
  [base-system & server-dependencies]
  (assoc base-system :server (c/using (new-server) (reduce conj [:config :handler] server-dependencies))))
