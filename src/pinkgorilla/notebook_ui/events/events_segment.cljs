(ns pinkgorilla.notebook-ui.events.events-segment
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [pinkgorilla.notebook-ui.events.events :refer [current-notebook change-current-notebook]]))

(defn notebook-segment-active [document]
  (let [active (:active document)
        segment (when (and document active)
                  (get-in document [:segments active]))]
    segment))

(defn segment-active [db]
  (-> db
      current-notebook
      notebook-segment-active))

(defn segment-op [db fun]
  (let [document (current-notebook db)
        active (:active document)
        segment (when (and document active)
                  (get-in document [:segments active]))]
    (if segment
      (let [segment-new (fun segment)]
        ;(info "document: " d)
        (change-current-notebook
         db
         (assoc-in document [:segments active] segment-new)))
      (do (error "cannot do segment-op: no active segment!")
          db))))

(defn codemirror-segment-op [db fun]
  (let [document (current-notebook db)
        active (:active document)
        segment (when (and document active)
                  (get-in document [:segments active]))
        {:keys [id cm]} (:codemirror db)
        cm? (and cm (= id (:id segment)))]
    (if cm?
      (let [segment-new (fun document segment cm)]
        ;(info "document: " d)
        (change-current-notebook
         db
         (assoc-in document [:segments active] segment-new)))
      (do (error "cannot do codemirror-segment-op: no active cm segment!")
          db))))







