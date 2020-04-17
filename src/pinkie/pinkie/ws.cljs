(ns pinkie.ws
  (:require
   [clojure.string :as str]
   [cljs.core.async :as async  :refer (<! >! put! chan)]
   [re-frame.core :refer [reg-event-db dispatch-sync dispatch]]
   [taoensso.encore :as encore :refer-macros (have have?)]
   [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf)]
   [taoensso.sente :as sente :refer (cb-success?)]
   [taoensso.sente.packers.transit :as sente-transit] ;; Optional, for Transit encoding
   )
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)]))


;; stolen from metasouarous oz.

(debugf "connecting sente websocket..")

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

(let [packer (sente-transit/get-transit-packer)
      {:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
       "/chsk" ; Must match server Ring routing URL
       ?csrf-token
       {:type :auto  ; :ajax
        :packer packer})]
  (def chsk chsk)
  (def ch-chsk ch-recv)
  (def chsk-send! send-fn)
  (def chsk-state state))

(defmulti -event-msg-handler :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  ;(debugf "Event: %s" event)
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [event]}]
  (debugf "Unhandled event: %s" event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (debugf "Channel socket successfully established!: %s" ?data)
      (debugf "Channel socket state change: %s" ?data))))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (debugf "Handshake: %s" ?data)))



;; This is the main event handler; If we want to do cool things with other kinds of data going back and forth,
;; this is where we'll inject it.
(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (let [[id msg] ?data]
    (case id
      ;:oz.core/view-spec (swap! app-state assoc :view-spec msg)
      :pinkie/heartbeat (debugf "Pinkie Heartbeat: %s" msg)
      :pinkie/code-add (dispatch [:notebook-add-code msg])
      :pinkie/result-set (dispatch [:notebook-set-result msg])
      (debugf "Pinkie Unhandled server event: %s" ?data))))


;;;; Sente event router (our `event-msg-handler` loop)

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-client-chsk-router!
           ch-chsk event-msg-handler)))


(debugf "testing 123...")


(defn send! [data]
  (chsk-send! data 5000
              (fn [cb-reply]
                (debugf "Callback reply: %s" cb-reply))))