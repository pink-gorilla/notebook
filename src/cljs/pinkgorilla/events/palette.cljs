(ns pinkgorilla.events.palette
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))


(defn- reset-palette
  [db]
  (let [palette (:palette db)]
    (assoc-in db
              [:palette]
              (merge palette {:show          false
                              :highlight     0
                              :visible-items (:all-visible-commands palette)
                              :filter        ""}))))

(reg-event-db
 :palette-blur
 [standard-interceptors]
 (fn [db [_]]
   (reset-palette db)))

(reg-event-db
 :palette-filter-changed
 (fn [db [_ val]]
   (let [palette (:palette db)]
     (assoc-in db
               [:palette]
               (merge palette {:show          true
                               :visible-items (->> (:all-items palette)
                                                   (filter (partial text-matches-re val)))
                               :filter        val})))))

(reg-event-db
 :palette-filter-keydown
 (fn [db [_ keycode]]
   (let [palette (:palette db)
         hl (:highlight palette)
         items (:visible-items palette)
         maxidx (- (count items) 1)]
     (case keycode
       38 (assoc-in db [:palette] (merge palette
                                         {:highlight (if (> hl 0)
                                                       (- hl 1)
                                                       hl)})) ;; up
       40 (assoc-in db [:palette] (merge palette
                                         {:highlight (if (< hl maxidx)
                                                       (+ hl 1)
                                                       hl)})) ;; down
       27 (reset-palette db)                               ;; esc
       13 (let [item (if (not-empty items) (nth items hl))
                handler (:handler item)]
            (if handler
              (if (string? handler)
                (do
                   ;; Gotcha Cannot call dispatch-sync in event handler
                  (dispatch [(keyword handler)])
                  (reset-palette db))
                (do
                  (handler (reset-palette db))))
              db))
       db))))

(reg-event-db
 :palette-action
 (fn [db [_ command]]
   (let [handler (:handler command)]
     (if (string? handler)
        ;; Gotcha : no dispatch-sync in handler
       (do
         (dispatch [(keyword handler)])
         (reset-palette db))
       (handler (reset-palette db))))))

;;
;; App commands
;;

(reg-event-db
 :app:commands
 [standard-interceptors]
 (fn [db _]
   (let [palette (:palette db)]
     (assoc-in db [:palette]
               (merge palette {:show          true
                               :all-items     (:all-visible-commands palette)
                               :visible-items (:all-visible-commands palette)
                               :label         "Choose a command:"})))))
