(ns pinkgorilla.notebook-app.system
  (:require
   [com.stuartsierra.component :as component]
   [de.otto.tesla.system :as otto-system]
   [pinkgorilla.notebook-app.dispatcher :refer [new-dispatcher]]
   [pinkgorilla.kernel.nrepl :as nrepl :refer [new-cider-repl-server]]
   [pinkgorilla.serving-with-jetty :as jetty :refer [add-jetty-server]])
  (:gen-class))

(def system (atom nil))

;; (keys @gorilla-system)
(defn get-in-system [path]
  (get-in @system path))

(defn get-setting [path]
  (get-in @system (vec (concat [:config :config :settings] path))))

(defn gorilla-system [runtime-config]
  (-> (otto-system/base-system (merge {:name "gorilla-service"} runtime-config))
      (assoc
       :dispatching (component/using
                     (new-dispatcher)
                     [:handler :nrepl-service :config :app-status]))
      (assoc
       :nrepl-service (component/using
                       (new-cider-repl-server)
                       [:config :metering :app-status]))
      (add-jetty-server)))

(defn start [config]
  ;; TODO: We just support one system/process for the moment
  (reset! system
          (otto-system/start
           (gorilla-system config))))
