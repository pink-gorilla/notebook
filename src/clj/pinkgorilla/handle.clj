;;;; This file is part of gorilla-repl. Copyright (C) 2015-, Gorilla REPL contributors.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

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
    [pinkgorilla.files :as files]
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

;; rebindable for testing
(defn read-sheet-locally
  [ws-file]
  (slurp (str ws-file) :encoding "UTF-8"))

(defn persist
  [ws-file ws-data]
  (spit ws-file ws-data))

(defn load-worksheet
  [req]
  ;; TODO: S'pose some error handling here wouldn't be such a bad thing
  (when-let [ws-file (:worksheet-filename (:params req))]
    (let [_ (info (str "Loading: " ws-file " ... "))
          ws-data (if (re-find #"^http[s]*" ws-file)
                    (slurp ws-file)
                    ;; (:body @(http/get ws-file))
                    (read-sheet-locally ws-file))
          _ (info "done.")]
      (res/response {:worksheet-data ws-data}))))


;; the client can post a request to have the worksheet saved, handled by the following
(defn save
  [req]
  ;; TODO: error handling!
  (when-let [ws-data (:worksheet-data (:params req))]
    (when-let [ws-file (:worksheet-filename (:params req))]
      (info (str "Saving: " ws-file " ... "))
      (persist ws-file ws-data)
      (info (str "done"))
      (res/response {:status "ok"}))))

;; More ugly atom usage to support defroutes
(def ^:private excludes (atom #{".git"}))

;; configuration information that will be made available to the webapp
(def ^:private conf (atom {}))

;; API endpoint for getting webapp configuration information
(defn config
  [req]
  (res/response @conf))

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

;; API endpoint for getting the list of worksheets in the project
(defn gorilla-files
  [req]
  (let [excludes @excludes]
    (res/response {:files (files/gorilla-filepaths-in-current-directory excludes)})))

;; configuration information that will be made available to the webapp
(defn set-config
  [k v]
  (swap! conf assoc k v))

(defn update-excludes
  [fn]
  (swap! excludes fn))
