(ns gorilla-repl.middleware
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

;; use api-defaults for to get around "Invalid anti-forgery token" caused by site-defaults
(defn wrap-middleware [handler]
  (wrap-defaults handler api-defaults))
