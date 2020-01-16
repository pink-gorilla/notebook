(ns pinkgorilla.handle
  "Ring handlers for Gorilla server functions. Broken out into their own namespace so as to be usable without having to
  use Gorilla's embedded HTTPKit server. Note that as well as the handlers there are two functions in this NS,
  `update-excludes` and `set-config` that modify the state returned by handlers."
  (:require
   #_[taoensso.timbre :as timbre
      :refer (log trace debug info warn error fatal report
                  logf tracef debugf infof warnf errorf fatalf reportf
                  spy get-env log-env)]
    ;; [ring.middleware.keyword-params :as keyword-params]
   [ring.middleware.cors :refer [wrap-cors]]
    ;; [ring.middleware.params :as params]
   [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
   ;; [ring.middleware.json :as json]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.util.response :as res]
   [pinkgorilla.system :as sys]))

(defn redirect-app
  [_]
  (res/redirect (str "." gorilla_repl.GorillaReplListener/PREFIX "worksheet.html")))

;; a wrapper for JSON API calls
(defn wrap-api-handler
  [handler]
  (-> handler
      (wrap-defaults api-defaults)
      (wrap-restful-format :formats [:json :transit-json :edn])
      #_(json/wrap-json-response)))

(defn wrap-cors-handler
  [handler]
  (wrap-cors handler
             :access-control-allow-origin [#".*"]
             :access-control-allow-methods [:get]))


;; API endpoint for getting webapp configuration information


(defn config
  [_]
  (res/response (sys/get-in-system [:config :config :settings])))
