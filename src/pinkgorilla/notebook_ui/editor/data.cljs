(ns pinkgorilla.notebook-ui.editor.data
  (:require
   [taoensso.timbre :refer-macros [debugf info error]]
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.events.events :refer [notebook-op current-notebook]]
   [pinkgorilla.notebook-ui.editor.debounce :refer [debounce]]))

(rf/reg-sub
 :segment/data
 (fn [db [_ seg-id type]]
   (let [nb (current-notebook db)
         content (get-in nb [:segments seg-id type])]
     (or content (str "content seg-id: " seg-id " type: " type)))))

(rf/reg-event-db
 :segment/set-data
 (fn [db [_ seg-id type content]]
   (notebook-op db #(assoc-in % [:segments seg-id type] content))))

(defn get-data [type id]
  (info "get-data id:" id "type:" type)
  (let [s (rf/subscribe [:segment/data id type])]
    @s))

(defn save-data [type id content]
  ;(println "saving md: " content)
  (rf/dispatch [:segment/set-data id type content]))

(def save-data-debounced
  (debounce save-data 1000))