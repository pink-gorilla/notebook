(ns pinkgorilla.notebook-ui.views.segment-nav
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [subscribe dispatch]]))

(defn segments-ordered [notebook]
  (let [segments (:segments notebook)
        segment-ids-ordered (:order notebook)]
    (vec (map #(get segments %) segment-ids-ordered))))

(defn icon [active-segment-id current-segment-id]
  (let [active? (= active-segment-id current-segment-id)]
    (if active?
      [:i.fas.fa-circle.ml-1]
      [:i.far.fa-circle.ml-1
       {:on-click #(dispatch [:notebook/move :to current-segment-id])}])))

(defn segment-nav-impl [segment-active notebook]
  (let [;_ (debug "active: " segment-active)
        segment-ids-ordered (:order notebook)
        ;_ (debug "order: " segment-ids-ordered)
        ]
    (into [:div]
          (map (partial icon segment-active) segment-ids-ordered))))

(defn segment-nav []
  (let [notebook @(subscribe [:notebook])
        ;_ (debug "nb: " notebook)
        active-segment @(subscribe [:notebook/segment-active])
        active-segment-id (:id active-segment)]
    [segment-nav-impl active-segment-id notebook]))
