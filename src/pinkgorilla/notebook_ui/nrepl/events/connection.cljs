(ns pinkgorilla.notebook-ui.nrepl.events.connection
  (:require
   [taoensso.timbre :refer-macros [debug info warnf warn errorf]]
   [clojure.string :as str]
   [cemerick.url :as url]
   [cljs.core.async :as async :refer [<! >! chan timeout close!]]
   ;[reagent.core :as r]
   ;[reagent.ratom :refer [make-reaction]]
   [re-frame.core :refer [reg-event-fx reg-event-db dispatch]]
   [pinkgorilla.nrepl.client.core :refer [connect send-request! request-rolling!]]))

(defn application-url []
  (url/url (-> js/window .-location .-href)))

(defn ws-path [port path]
  (let [app-url (application-url)
        proto (if (= (:protocol app-url) "http") "ws" "wss")
        port (or port (:port app-url))
        port-postfix (if (< 0 port)
                       (str ":" port)
                       "")
        wsp (str proto "://" (:host app-url) port-postfix path)]
    (info "nrepl ws-endpoint: " wsp)
    wsp))

(reg-event-db
 :nrepl/init
 (fn [db [_]]
   (let [db (or db {})
         api (get-in db [:config :profile :server :api])
         port-api (get-in db [:config :web-server-api :port])
         port (when api port-api)
         config (get-in db [:config :nrepl])
         {:keys [ws-endpoint connect?]} config
         ws-endpoint (or ws-endpoint (ws-path port "/api/nrepl"))]
     (dispatch [:kernel/register-kernel :clj :nrepl/eval])

     (if connect?
       (do
         (warn "auto-connect nrepl: " ws-endpoint)
         (dispatch [:nrepl/connect]))
       (warn "NOT connecting automatically to nrepl"))

     (assoc db :nrepl {:ws-url ws-endpoint
                       :connected? false
                       :conn nil
                       :info {:sessions nil  ; this needs to be here, otherwise ops fail
                              :describe nil}}))))

(reg-event-fx
 :nrepl/sniffer-sink
 (fn [{:keys [db] :as cofx}  [_]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (info "registering sniffer-sink")
     ;(dispatch [:nrepl/op-dispatch-rolling {:op "eval" :code "(+ 7 7)"} [:sniffer/rcvd]])
     (dispatch [:nrepl/op-dispatch-rolling {:op "sniffer-sink"} [:sniffer/rcvd]])
     nil)))

(reg-event-db
 :nrepl/set-connection-status
 (fn [db [_ connected?]]
   (let [old-connected? (get-in db [:nrepl :connected?])]
     (if (= old-connected? connected?)
       (do (warnf ":nrepl/set-connection-status - connection status unchanged: %s" connected?)
           db)
       (do
         (if connected?
           (do
             (info "nrepl connected!")
             (dispatch [:nrepl/sniffer-sink]))
           (info "nrepl disconnected!"))
         (assoc-in db [:nrepl :connected?] connected?))))))

(reg-event-db
 :nrepl/connect
 (fn [{:keys [nrepl] :as db} [_]]
   (let [{:keys [ws-url]} nrepl
         {:keys [conn] :as c} (connect {:ws-url ws-url})]

     (add-watch conn :my-watch (fn [c]
                                 (let [{:keys [connected?]} @conn]
                                   (debug "nrepl ws connected? " connected?)
                                   (dispatch [:nrepl/set-connection-status connected?]))))
     (assoc-in db [:nrepl :conn] c))))

(reg-event-db
 :nrepl/connect-to
 (fn [db [_ ws-url]]
   (let [db (or db {})]
     (dispatch [:nrepl/connect])
     (assoc-in db [:nrepl :ws-url] ws-url))))









