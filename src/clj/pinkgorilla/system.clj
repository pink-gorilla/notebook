(ns pinkgorilla.system
  (:require
   [com.stuartsierra.component :as component]
   [de.otto.tesla.system :as system]
   [pinkgorilla.dispatcher :as dispatcher]
   [pinkgorilla.nrepl :as nrepl]
   [pinkgorilla.serving-with-jetty :as jetty])
  (:gen-class))

(def system (atom nil))

;; (keys @gorilla-system)
(defn get-in-system [path]
  (get-in @system path))

(defn get-setting [path]
  (get-in @system (vec (concat [:config :config :settings] path))))

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
      (jetty/add-server)))

(defn start [config]
  ;; TODO: We just support one system/process for the moment
  (reset! system
          (system/start
           (gorilla-system config))))
