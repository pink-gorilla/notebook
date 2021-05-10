(ns pinkgorilla.notebook-ui.events.events-move
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [dispatch reg-event-db]]
   [pinkgorilla.notebook-ui.events.events :refer [notebook-op]]))

(defn move [order current-segment-id direction]
  (info "move " current-segment-id direction  " order: " order)
  (let [v-indexed (map-indexed (fn [idx id] [idx id]) order)
        ;_ (info "v-indexed: " v-indexed)
        current (first
                 (filter
                  (fn [[idx id]] (= current-segment-id id))
                  v-indexed))
        ;_ (info "current: " current)
        current-idx (get current 0)
        lookup (into {} v-indexed)
        ;_ (info "lookup: " lookup)
        idx-target (case direction
                     :down (min (+ current-idx 1) (- (count order) 1))
                     :up (max 0 (- current-idx 1)))
        ;_ (info "idx-target: " idx-target)
        ]
    (info "new current: " (get lookup idx-target))
    (get lookup idx-target)))


;(.scrollTo js/window (0, tesNode.offsetTop);


#_(reg-event-db
   :notebook/scroll-to
   (fn [db [_]]
     (let [storage (:notebook db)
           notebook (get-in db [:document :documents storage])
           active (:active notebook)
           active-id (str active)]
       (when active
         (when-let [node (.getElementById js/document active-id)]
           (doto node
             (.scrollIntoView))))
       db)))

(reg-event-db
 :notebook/move
 (fn [db [_ direction id]]
   (let [fun (case direction
               :up #(move (:order %) (:active %) direction)
               :down #(move (:order %) (:active %) direction)
               :first #(first (:order %))
               :last #(last (:order %))
               :to (fn [_] id))
         assoc-active (fn [doc]
                        (assoc doc :active (fun doc)))]
     ;(dispatch [:notebook/scroll-to])
     (if fun
       (notebook-op db assoc-active)
       db))))
