(ns ^:no-doc pinkie.server
  (:require
   [clojure.core.async :as async  :refer (<! <!! >! >!! put! chan go go-loop)]
   [clojure.java.io :as io]

   [clojure.string :as str]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.keyword-params]
   [ring.middleware.params]
;   [ring.middleware.cljsjs :refer [wrap-cljsjs]]
  ; [ring.middleware.anti-forgery :as af :refer :all]
   [ring.middleware.anti-forgery :refer (*anti-forgery-token*)]
   [ring.util.response :as response]
   [compojure.core :as comp :refer (defroutes GET POST)]
   [compojure.route :as route]
   [aleph.http :as aleph]

   [taoensso.encore :as encore :refer (have have?)]
   [taoensso.timbre :as log :refer (tracef debugf infof warnf errorf)]
   [taoensso.sente  :as sente]
   [taoensso.sente.server-adapters.aleph :refer (get-sch-adapter)]
   [taoensso.sente.packers.transit :as sente-transit]

   [cheshire.core :as json]

   ;[oz.live :as live]
   )
  (:gen-class))

;; stolen from metasouarous oz.

(def default-port 10666)

;(log/set-level! :info)
;; (log/set-level! :debug)
(taoensso.timbre/set-level! :trace) ; Uncomment for more logging


(reset! sente/debug-mode?_ true) ; Uncomment for extra debug info

; packer :edn

(let [packer (sente-transit/get-transit-packer)
      chsk-server (sente/make-channel-socket-server! 
                   (get-sch-adapter) 
                   {:packer packer
                    :csrf-token-fn nil ; awb99; disable CSRF checking.
                    })
      {:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]} chsk-server]
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv)
  (def chsk-send! send-fn)
  (def connected-uids connected-uids))

(defn send-all!
  [data]
  (doseq [uid (:any @connected-uids)]
    (chsk-send! uid data)))

(add-watch connected-uids :connected-uids
           (fn [_ _ old new]
             (when (not= old new)
               (infof "Connected uids change: %s" new))))

(defn connected-uids? []
  @connected-uids)

(defn unique-id
  "Get a unique id for a session."
  []
  (str (java.util.UUID/randomUUID)))

(defn session-uid
  "Get session uuid from a request."
  [req]
  (get-in req [:session :uid]))


(defonce current-root-dir (atom ""))


; (println "anti-forgery-token:" *anti-forgery-token*)

(defroutes my-routes
  (GET  "/" req (response/content-type
                 {:status 200
                  :session (if (session-uid req)
                             (:session req)
                             (assoc (:session req) :uid (unique-id)))
                  :body (io/input-stream (io/resource "gorilla-repl-client/pinkie.html"))}
                 "text/html"))

  (GET "/token" req (json/generate-string {:csrf-token *anti-forgery-token*}))

  (GET  "/chsk" req
    (debugf "/chsk got: %s" req)
    (let [r (ring-ajax-get-or-ws-handshake req)]
      (println "ws init: " r)
      (println "token: " (get-in req [:session :ring.middleware.anti-forgery/anti-forgery-token]))
      r))

  (POST "/chsk" req (ring-ajax-post req))

  (route/files "/" {:root "./target/cljsbuild/gorilla-repl-client/"})
  (route/resources "/" {:root "gorilla-repl-client"})
  #_(GET "*" req (let [reqpath (live/join-paths @current-root-dir (-> req :params :*))
                       reqfile (io/file reqpath)
                       altpath (str reqpath ".html")
                       dirpath (live/join-paths reqpath "index.html")]
                   (cond
                   ;; If the path exists, use that
                     (and (.exists reqfile) (not (.isDirectory reqfile)))
                     (response/file-response reqpath)
                   ;; If not, look for a `.html` version and if found serve that instead
                     (.exists (io/file altpath))
                     (response/content-type (response/file-response altpath) "text/html")
                   ;; If the path is a directory, check for index.html
                     (and (.exists reqfile) (.isDirectory reqfile))
                     (response/file-response dirpath)
                   ;; Otherwise, not found
                     :else (response/redirect "/"))))
  (route/not-found "<h1>There's no place like home</h1>"))


(def main-ring-handler
  (-> my-routes
      ;(wrap-defaults site-defaults)
      (wrap-defaults
       (-> site-defaults
           (assoc-in [:security :anti-forgery] true)))
      ;(ring.middleware.keyword-params/wrap-keyword-params)
      ;(ring.middleware.params/wrap-params)
      ;(wrap-cljsjs)
      (wrap-gzip)))



(defmulti -event-msg-handler :id)

(defn event-msg-handler [{:as ev-msg :keys [id ?data event]}]
  (tracef "Event: %s" event)
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid (:uid session)]
    (tracef "Unhandled event: %s" event)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-from-server event}))))

(defonce router_ (atom nil))
(defn stop-router! [] (when-let [stop-fn @router_] (stop-fn)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-server-chsk-router! ch-chsk event-msg-handler)))


;; web server

(defonce web-server_ (atom nil))
(defn web-server-started? [] @web-server_)
(defn stop-web-server! [] (when-let [stop-fn (:stop-fn @web-server_)] (stop-fn)))
(defn start-web-server! [& [port]]
  (stop-web-server!)
  (let [port (or port default-port) ; 0 => Choose any available port
        ring-handler (var main-ring-handler)
        [port stop-fn]
        (let [server (aleph/start-server ring-handler {:port port})
              p (promise)]
          (future @p) ; Workaround for Ref. https://goo.gl/kLvced
          ;; (aleph.netty/wait-for-close server)
          [(aleph.netty/port server)
           (fn [] (.close ^java.io.Closeable server) (deliver p nil))])
        uri (format "http://localhost:%s/" port)]
    (infof "Web server is running at `%s`" uri)
    (reset! web-server_ {:port port :stop-fn stop-fn})
    ; (clojure.java.browse/browse-url uri)
    #_(try
      (if (and (java.awt.Desktop/isDesktopSupported)
               (.isSupported (java.awt.Desktop/getDesktop) java.awt.Desktop$Action/BROWSE))
        (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
        (.exec (java.lang.Runtime/getRuntime) (str "xdg-open " uri)))
      (Thread/sleep 7500)
      (catch java.awt.HeadlessException _))))


(defn get-server-port [] (:port @web-server_))


(defn stop! []
  (stop-router!)
  (stop-web-server!))

(defn start-server!
  "Start the oz plot server (on localhost:10666 by default)."
  ([]
   (start-web-server! default-port)
   (start-router!))
  ([& [port]]
   (start-web-server! (or port default-port))
   (start-router!)))

; CSRF
; awb99: sente csrf requires sending a dynamically rendered html page,
;        that contains inside the csrf token.
;
; (GET  "/"      ring-req (landing-pg-handler            ring-req))
;
; (defn landing-pg-handler [ring-req]
;  (hiccup/html
;    [:h1 "Sente reference example"]
;    (let [csrf-token
;          ;; (:anti-forgery-token ring-req) ; Also an option
;          (force anti-forgery/*anti-forgery-token*)]
;      [:div#sente-csrf-token {:data-csrf-token csrf-token}])


;; Heartbeat sender

(def broadcast-enabled?_ (atom true))

(defn start-heartbeats!
   "setup a loop to broadcast an event to all connected users every second"
  []
  (let [broadcast!
        (fn [i]
          (let [uids (:any @connected-uids)]
            (debugf "Broadcasting Heartbeats to %s users." (count uids))
            (doseq [uid uids]
              (chsk-send! uid
                          [:pinkie/heartbeat
                           {:uid uid
                            :i i}]))))]

    (go-loop [i 0]
      (<! (async/timeout 10000))
      (when @broadcast-enabled?_ (broadcast! i))
      (recur (inc i)))))


