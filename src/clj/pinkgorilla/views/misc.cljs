(ns pinkgorilla.views.misc
  (:require [re-frame.core :as re-frame]))

(defn big-button
  [{:keys [dispatch class icon] :or {class "is-primary"}} label]
  [:div.columns
   [:div.column.is-half.is-offset-one-quarter
    [:a.button.is-medium {:on-click #(re-frame/dispatch dispatch)
                          :class class}
     [:span.icon
      [:i.fa {:class icon}]]
     [:span label]]]])