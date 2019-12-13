(ns pinkgorilla.system
  (:require
   ;; [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [de.otto.tesla.system :as system]
   [de.otto.tesla.serving-with-jetty :as tesla-jetty]
   ;; [de.otto.tesla.serving-with-httpkit :as httpkit]
   [pinkgorilla.dispatcher :as dispatcher]
   [pinkgorilla.nrepl :as nrepl]
   ;; [pinkgorilla.cli :as cli]
   [pinkgorilla.serving-with-jetty :as jetty])
  (:gen-class))

(def system (atom nil))

;; (keys @gorilla-system)
(defn get-in-system [path]
  (get-in @system path))

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
  ;; We just support one system for the moment
  (reset! system
          (system/start
            (gorilla-system config))))
