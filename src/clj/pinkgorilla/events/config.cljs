(ns pinkgorilla.events.config
  "Events related to app-db init configuration loading"
  (:require
   [taoensso.timbre :refer-macros (info)]
   [clojure.string :as str]
   [ajax.core :as ajax]
   [re-frame.core :refer [reg-event-db reg-event-fx]]
  ;PinkGorilla Notebook
   [pinkgorilla.db :as db :refer [initial-db]]
   [pinkgorilla.keybindings :as keybindings]
   ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   ))

; initialize app-db

(defn- init-app-db
  [app-url]
  (let [base-path (str/replace (:path app-url) #"[^/]+$" "")
        db (merge initial-db {:base-path base-path})]
    db))

(reg-event-db
 :initialize-app-db
 (fn [_ [_ app-url]]
   (info "initializing app-db ..")
   (init-app-db app-url)))

; load configuration


(reg-event-fx
 :load-config
 (fn [{:keys [db]} _]
   ;(add-notification (notification :info "Loading config.. "))
   (info "loading configuration from server ..")
   {:db       db
    :http-xhrio {:method          :get
                 :uri             (str (:base-path db) "config")
                 :timeout         5000                     ;; optional see API docs
                 :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:process-config-response]
                 :on-failure      [:process-error-response "load-config"]}}))


; used by process config reponse below.


(def install-commands
  (re-frame.core/->interceptor
   :id :install-commands
   :after (fn
            [context]
            (keybindings/install-commands (get-in context [:effects :db :config :cljs :key-bindings]))
            context)))

(reg-event-fx
 :process-config-response
 [install-commands]
 (fn [cofx [_ response]]
   (let [all-bindings (get-in response [:cljs :key-bindings])
         visible-commands (keybindings/visible-commands all-bindings)]
     {:db         (-> (assoc-in (:db cofx) [:config] response)
                      (assoc-in [:palette :all-visible-commands] visible-commands))
      :dispatch-n (list [:init-cljs] [:explore-load])})))



