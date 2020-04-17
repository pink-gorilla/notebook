(ns pinkgorilla.middleware
  (:require 
     [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(println "production profile!")

;;ignore println statements in prod
;(set! *print-fn* (fn [& _]))

;; use api-defaults for to get around "Invalid anti-forgery token" caused by site-defaults
(defn wrap-middleware [handler]
  (wrap-defaults handler api-defaults))
