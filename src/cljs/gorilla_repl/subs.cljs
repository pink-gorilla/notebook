(ns gorilla-repl.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub-raw reg-sub]]))

;; -- Subscription handlers

(reg-sub
    :docs
    (fn [db _]
      (:docs db)))

(reg-sub
  :config
  (fn [db _]
    (:config db)))

(reg-sub
  :palette
  (fn [db _]
    (:palette db)))

(reg-sub
  :message
  (fn [db _]
    (:message db)))

(reg-sub
  :worksheet
  (fn [db _]
    (get-in db [:worksheet])))

(reg-sub
  :save-dialog
  (fn [db _]
    (get-in db [:save])))

(reg-sub
  :segment-query
  (fn [db [_ seg-id]]
    (get-in db [:worksheet :segments seg-id])))

(reg-sub
  :is-active-query
  (fn [db [_ seg-id]]
    (= seg-id (get-in db [:worksheet :active-segment]))))

(reg-sub
  :is-queued-query
  (fn [db [_ seg-id]]
    (contains? (get-in db [:worksheet :queued-code-segments]) seg-id)))
