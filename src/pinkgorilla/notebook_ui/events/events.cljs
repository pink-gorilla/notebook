(ns pinkgorilla.notebook-ui.events.events
  (:require
   [taoensso.timbre :as timbre :refer-macros [info error]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]))

(reg-event-db
 :notebook/activate!
 (fn [db [_ storage]]
   (info "setting current storage: " storage)
   (assoc-in db [:notebook] storage)))

(defn current-notebook [db]
  (let [storage (:notebook db)
        document (get-in db [:document :documents storage])]
    document))

(defn change-current-notebook [db notebook]
  (let [storage (:notebook db)]
    (assoc-in db [:document :documents storage] notebook)))

(defn notebook-op [db fun]
  (let [document (current-notebook db)]
    (if document
      (let [d (fun document)]
        ;(info "document: " d)
        (change-current-notebook db d))
      (do (error "cannot do notebook-op: no active document!")
          db))))

(reg-event-db
 :notebook/meta-set
 (fn [db [_ k v]]
   (info "changing notebook meta " k " to: " v)
   (notebook-op db #(assoc-in % [:meta k] v))))

(reg-event-fx
 :notebook/layout
 (fn [_ [_ layout]]
   (dispatch [:settings/set :layout layout])))


