(ns pinkgorilla.system
  (:require 
     [de.otto.tesla.system :as system]
     [pinkgorilla.dispatcher :as dispatcher]
     [pinkgorilla.nrepl :as nrepl]
     ;; [pinkgorilla.cli :as cli]
     ;; [clojure.tools.logging :as log]
     [pinkgorilla.serving-with-jetty :as jetty]
     [de.otto.tesla.serving-with-jetty :as tesla-jetty]
     ;; [de.otto.tesla.serving-with-httpkit :as httpkit]
     [com.stuartsierra.component :as component])
  (:gen-class))


(defn gorilla-system [runtime-config]
  (-> (system/base-system (merge {:name "gorilla-service"} runtime-config))
      (assoc
        :dispatching (component/using
                       (dispatcher/new-dispatcher)
                       [:handler :nrepl-service :config :app-status]))
      (assoc
        :nrepl-service (component/using
                         (nrepl/new-cider-repl-server)
                         [:config :metering :app-status]))
      ;; Cannot use tesla here because its jetty appears to lacks websocket support
      (jetty/add-server)
      ;; (httpkit/add-server)
      ))

(defn start [config]
  (system/start (gorilla-system config)))
