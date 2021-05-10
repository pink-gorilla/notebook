
(ns pinkgorilla.notebook-ui.datafy.events-nrepl
  (:require
   [taoensso.timbre :refer-macros [info]]
   [cljs.core.async :as async :refer [<!] :refer-macros [go]]
   [re-frame.core :refer [reg-event-db dispatch]]
   [cljs.tools.reader.edn :as edn]
   [cljs.tagged-literals]
   [pinkgorilla.nrepl.client.core :refer [op-gorillanav op-eval send-request!]]))

(defn decode [datafy-str]
  (edn/read-string
   {:readers {;; 'js (with-meta identity {:punk/literal-tag 'js})
              'inst cljs.tagged-literals/read-inst
              'uuid cljs.tagged-literals/read-uuid
              'queue cljs.tagged-literals/read-queue}
    :default tagged-literal}
   datafy-str))

(defn run-op [conn op evt-name]
  (go
    (let [r (<! (send-request! conn op))
          ;_  (info "nrepl run-op result: " r)
          r (if (vector? r) (first r) r)
          datafy-str (:datafy r)
          datafy (when datafy-str
                   (info "decode datafy str: " datafy-str)
                   (decode datafy-str)) ;(reader/read-string datafy-str)  
          ]
      (info "decoded datafy: " datafy)
      (dispatch [evt-name datafy]))))

; tap
; nrepl eval that returns datafy root entry

(reg-event-db
 :datafy/tap
 (fn [db [_ code]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (info "tap: " code)
     (run-op conn (op-eval code) :datafy/tap-response)
     db)))

(reg-event-db
 :datafy/tap-response
 (fn [db [_ datafy]]
   (info "tap-response: " datafy)
   (dispatch [:punk/tap-response (:idx datafy) datafy])
   (-> db
       (assoc :datafy datafy)
       #_(update-in [:punk :entries] conj datafy))))

;; nav

(reg-event-db
 :datafy/nav
 (fn [db [_ idx k v]]
   (let [{:keys [nrepl]} db
         {:keys [conn]} nrepl]
     (info "nav idx: " idx " key: " k "v: " v)
     (run-op conn (op-gorillanav idx k v) :datafy/nav-response)
     db)))

(reg-event-db
 :datafy/nav-response
 (fn [db [_ datafy]]
   (info "nav-response: " datafy)
   (dispatch [:punk/nav-response (:idx datafy) datafy])
   db
   #_(-> db
         (assoc :datafy datafy)
         (update-in [:punk :entries] conj datafy))))






