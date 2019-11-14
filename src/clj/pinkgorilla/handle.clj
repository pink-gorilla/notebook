(ns pinkgorilla.handle
  "Ring handlers for Gorilla server functions. Broken out into their own namespace so as to be usable without having to
  use Gorilla's embedded HTTPKit server. Note that as well as the handlers there are two functions in this NS,
  `update-excludes` and `set-config` that modify the state returned by handlers."
  (:require
   [ring.middleware.keyword-params :as keyword-params]
   [ring.middleware.cors :refer [wrap-cors]]
   [ring.middleware.params :as params]
   [ring.middleware.defaults :refer [api-defaults site-defaults wrap-defaults]]
    ;; [ring.middleware.json :as json]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.util.response :as res]
    ;; [org.httpkit.client :as http]
    ;; [ring.middleware.webjars :refer [wrap-webjars]]
   [pinkgorilla.storage.files :as files]
   [clojure.tools.logging :refer (info)]
   [clojure.java.io :as io]
   #_[taoensso.timbre :as timbre
      :refer (log trace debug info warn error fatal report
                  logf tracef debugf infof warnf errorf fatalf reportf
                  spy get-env log-env)]))

(defn redirect-app
  [req]
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



;; configuration information that will be made available to the webapp
(def ^:private conf (atom {}))

;; API endpoint for getting webapp configuration information
(defn config
  [req]
  (res/response @conf))



;; configuration information that will be made available to the webapp
(defn set-config
  [k v]
  (swap! conf assoc k v))



(defn document-utf8
  [filename req]
  {:status  200
   ;; utf-8 needed HERE, content sets ISO-8859-1 default which
   ;; supercedes meta header in document
   ;; Session key is required to force setting the cookie
   :session (:session req)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (slurp (io/resource
                    (str "gorilla-repl-client/" filename)))})
