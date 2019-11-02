(ns gorilla-repl.dev-handle
  (:require
    [gorilla-repl.route :as r]
    [compojure.core :as compojure]
    ;; [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
    [prone.middleware :refer [wrap-exceptions]]
    [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-dev [handler]
  (-> handler
      ;; (wrap-defaults api-defaults)
      wrap-exceptions
      wrap-reload))

(def ^:private dynamic-handlers
  (apply compojure/routes
         (concat r/default-api-handlers
                 #_r/default-repl-handlers)))

(def dev-handler (wrap-dev
                   (apply compojure/routes
                          (concat r/default-api-handlers
                                  #_r/default-repl-handlers
                                  r/default-resource-handlers))))

#_(def dev-handler (apply compojure/routes
                          (concat r/default-api-handlers
                                  r/default-repl-handlers
                                  r/default-resource-handlers)))
