(ns pinkgorilla.events.settings
  "events related to the settings dialog"
  (:require
   [taoensso.timbre :refer-macros (info)]
   [cljs.reader :as reader]                                ; local storage parsing
   [re-frame.core :refer [reg-event-db dispatch]]
   [pinkgorilla.kernel.cljs :as cljs-kernel]
    ;[pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   ))

;; LocalStorage Helpers

(defn ls-set! [k v]
  (.setItem js/localStorage (pr-str k) (pr-str v)))

(defn ls-get [k]
  (when-let [s (.getItem js/localStorage (pr-str k))]
    (reader/read-string s)))

(defn ls-remove! [k]
  (.removeItem js/localStorage k))

;; Dialog Visibility Management

(reg-event-db
 :dialog-show
 (fn [db [_ dialog]]
   (assoc-in db [:dialog dialog] true)))

(reg-event-db
 :dialog-hide
 (fn [db [_ dialog]]
   (assoc-in db [:dialog dialog] false)))

;; Settings Dialog Visibility


(reg-event-db
 :app:hide-settings
 (fn [db _]
   (dispatch [:settings-localstorage-save])                ; save to localstorage on close of dialog.
   (assoc-in db [:dialog :settings] false)))

;; Settings Change

(reg-event-db
 :settings-localstorage-load
 (fn [db [_]]
   (let [_ (info "Loading Settings from Localstorage..")
         settings (ls-get :notebook-settings)]
     (if (nil? settings)
       (do (info "localstorage does not contain settings.")
           db)
       (do (info "loaded settings from localstorage: " settings)
           (assoc-in db [:settings] settings))))))

(reg-event-db
 :init-cljs
 (fn [db [_]]
   (let [_ (info "Init ClojureScript")
         settings (ls-get :notebook-settings)]
     (if (nil? settings)
       (do (info "localstorage does not contain settings.")
           db)
       (do (info "loaded settings from localstorage: " settings)
           (assoc-in db [:settings] settings)))
     (cljs-kernel/init! (get-in db [:config :cljs]))
     db)))

(reg-event-db
 :settings-localstorage-save
 (fn [db [_]]
   (let [settings (:settings db)]
     (ls-set! :notebook-settings settings)
     (info "localstorage settings saved: " settings)
     db)))

(reg-event-db
 :settings-set
 (fn [db [_ k v]]
   (info "changing setting " k " to: " v)
   (assoc-in db [:settings k] v)))

(reg-event-db
 :meta-set
 (fn [db [_ k v]]
   (info "changing notebook meta " k " to: " v)
   (assoc-in db [:worksheet :meta k] v)))
