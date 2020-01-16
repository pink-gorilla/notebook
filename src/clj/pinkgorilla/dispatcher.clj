(ns pinkgorilla.dispatcher
  (:require
   [com.stuartsierra.component :as c]
   [de.otto.tesla.stateful.handler :as handlers]
   [de.otto.status :as status]
   [de.otto.tesla.stateful.app-status :as app-status]))

(defrecord Dispatcher [routes]
  c/Lifecycle
  (start [self]
    ;; TODO: Hmpf not quite sure whether we really need or want this
    (let [s (-> (get-in self [:config :config :routes]) symbol)
          _ (-> s namespace symbol require)]
      (handlers/register-handler (:handler self)
                                 (resolve s))
      (app-status/register-status-fun (:app-status self)
                                      (fn [] (status/status-detail :dispatching :ok "page is always fine"))))
    self)
  (stop [self]
    self))

(defn new-dispatcher [] (map->Dispatcher {}))
