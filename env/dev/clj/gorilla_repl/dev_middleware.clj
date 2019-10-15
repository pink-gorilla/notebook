(ns gorilla-repl.dev-middleware
  (:require
    [gorilla-repl.route :as r]
    [compojure.core :as compojure]
    [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
    [prone.middleware :refer [wrap-exceptions]]
    [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-defaults api-defaults)
      wrap-exceptions
      wrap-reload))

(def ^:private dynamic-routes
  (apply compojure/routes
         (concat r/default-api-routes
                 r/default-repl-routes))
  #_(wrap-middleware (apply compojure/routes
                            (concat r/default-api-routes
                                    r/default-repl-routes))))

#_(def dev-hand (apply compojure/routes
                     (conj dynamic-routes
                           r/default-resource-routes)))

(def dev-handler (wrap-middleware
                 (apply compojure/routes (concat r/default-api-routes
                                                 r/default-repl-routes
                                                 r/default-resource-routes))))

#_(def dev-handler (apply compojure/routes (concat r/default-api-routes
                                                 r/default-repl-routes
                                                 r/default-resource-routes)))
