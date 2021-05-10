(ns pinkgorilla.notebook-ui.subscriptions
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-sub subscribe]]
   [pinkgorilla.notebook-ui.events.events :refer [current-notebook]]
   [pinkgorilla.notebook-ui.events.events-segment :refer [segment-active notebook-segment-active]]))

(reg-sub
 :document/current
 (fn [db _]
   (:notebook db)))

(reg-sub
 :notebook
 (fn [db _]
   (current-notebook db)))

#_(reg-sub
   :notebook/segment-active
   (fn [db _]
     (segment-active db)))

(reg-sub
 :notebook/segment-active
 :<- [:notebook]
 (fn [notebook _]
   (notebook-segment-active notebook)))

;notebook-segment-active

#_(reg-sub
   :notebook/queued  ; all queued segments in current notebook
   (fn [db _]
     (let [notebook (current-notebook db)
           queued (get-in notebook [:queued])]
     ;(info "queued: " queued)
       queued)))

(reg-sub
 :notebook/queued  ; all queued segments in current notebook
 :<- [:notebook]
 (fn [notebook _]
   (get-in notebook [:queued])))

(reg-sub
 :segment/queued? ; is seg-id queued ?
 (fn [_ seg-id]
   [(subscribe [:notebook/queued])]) ; reuse subscription :notebook/queued
 (fn [[queued] [_ seg-id]]
   ;(info "queued: " queued)
   (some #(= seg-id %) queued)))

(reg-sub
 :notebook/edit?
 (fn [db [_]]
   (or (:notebook/edit? db) false)))
