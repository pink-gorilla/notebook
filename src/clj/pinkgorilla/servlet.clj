(ns pinkgorilla.servlet
  (:use compojure.core)
  (:require
   [clojure.tools.logging :as log]
   [ring.util.servlet :as servlet]
   [pinkgorilla.route :as route])
  (:import (javax.servlet ServletConfig)
           (javax.servlet.http HttpServletRequest HttpServletResponse))
  (:gen-class :name pinkgorilla.RingServlet
              :extends javax.servlet.http.HttpServlet
              :exposes-methods {init superInit}))

#_(defn default-handler
    [request]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    "Set a decent handler"})

(def ring-service (atom nil))

;; (servlet/set-handler (gorilla/get-handlers))
(defn set-handler
  [handler]
  (log/info "Creating new service method from handler " handler)
  (reset! ring-service (servlet/make-service-method handler)))

(defn -init
  ([this]
   (log/debug "Servlet initialized with no params")
   (.superInit this)
   (set-handler
    (route/war-handler (-> (.getServletContext this) .getContextPath))))

  ([this ^ServletConfig config]
   (log/debug "Servlet initialized with servlet config" config)
   (.superInit this config)
   (set-handler
    (route/war-handler (-> (.getServletContext this) .getContextPath)))))


(defn -service
  [this ^HttpServletRequest request ^HttpServletResponse response]
  (@ring-service this request response))

;; (defn -destroy
;;  [this]
;;  (log/debug "Servlet destroyed"))

