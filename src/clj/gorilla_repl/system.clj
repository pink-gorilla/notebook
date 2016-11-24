(ns gorilla-repl.system
  (:require [de.otto.tesla.system :as system]
            [gorilla-repl.dispatcher :as dispatcher]
            [gorilla-repl.nrepl :as nrepl]
    ;; [gorilla-repl.cli :as cli]
            [clojure.tools.logging :as log]
            [de.otto.tesla.serving-with-httpkit :as httpkit]
            [com.stuartsierra.component :as component])
  (:gen-class))


(defn gorilla-system [runtime-config]
  (-> (system/base-system (merge {:name "gorilla-service"} runtime-config))
      (assoc
        :dispatching (component/using (dispatcher/new-dispatcher) [:handler :nrepl-service :config :app-status])) ;; :metering
      (assoc
        :nrepl-service (component/using (nrepl/new-cider-repl-server) [:config :metering :app-status]))
      (httpkit/add-server)))

(defn start [config]
  (system/start (gorilla-system config)))
