(ns pinkie.notebook
  "process-instructions from pinkie clj server"
  (:require
   [re-frame.core :refer [reg-event-db dispatch-sync dispatch]]
   [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf debug)]
   ; pinkgorilla libraries   
   [pinkgorilla.notebook.core :refer [empty-notebook
                                      insert-segment-at remove-segment
                                      create-code-segment create-free-segment]] ; manipulate notebook
    ; pinkgorilla notebook
   [pinkgorilla.events.helper :refer [standard-interceptors]]))


(defn add-segment
  "adds a new segment to the end of a notebook
   to be moved to a hydrated notebook manipulation library"
  [worksheet new-segment]
  (let [segment-order (:segment-order worksheet)
        segments (:segments worksheet)
        new-id (:id new-segment)]
    (merge worksheet
           {:active-segment new-id
            :segments       (assoc segments new-id new-segment)
            :segment-order  (into [] (conj segment-order new-id))})))


(reg-event-db
 :notebook-add-code
 [standard-interceptors]
 (fn [db [_ {:keys [id code]}]]
   (let [_ (debugf "code-add id: %s code: %s" id code)
         ;_ (debug "db:" (pr-str db))
         worksheet-old (get-in db [:worksheet])
         _ (debug "worksheet old:" worksheet-old)
         new-segment (assoc (create-code-segment code) :id id)
         _ (debug "new segment: " new-segment)
         worksheet-new (add-segment worksheet-old new-segment)
         _ (debug "worksheet new:" worksheet-new)]
     (assoc-in db [:worksheet] worksheet-new)
     )))

(reg-event-db
 :notebook-set-result
 [standard-interceptors]
 (fn [db [_ {:keys [id result]}]]
   (let [_ (debugf "set-result id: %s result: %s" id result)
         ;_ (debug "db:" (pr-str db))
         segment-old (get-in db [:worksheet :segments id])
         _ (debug "segment old:" segment-old)
         segment-new (merge segment-old result)
         _ (debug "segment new: " segment-new)]
     (assoc-in db [:worksheet :segments id] segment-new))))
