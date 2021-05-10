(ns pinkgorilla.notebook-ui.views.segment-menu
  (:require
   [re-frame.core :refer [dispatch]]))

(defn cell-menu [segment]
  [:div {:class [:font-sans :flex :flex-col :text-center :sm:flex-row :sm:text-left :sm:justify-between :px-6 :bg-white :sm:items-baseline :w-full]}

   ;; Kernel switch (clj/cljs)
   [:div.mb-1
    [:a {:class [:text-lg :p-1 :pg-kernel-toggle]
         :on-click #(dispatch [:segment-active/kernel-toggle])}
     (:kernel @segment)]]

   [:div.mt-1.mb-1.h-8
   ;[:p {:class "text-lg no-underline text-grey-darkest hover:text-blue-dark ml-2"} "One" ]

    [:a {:class [:pg-cell-actions :lg:inline]
         :on-click #(do
                      (dispatch [:segment/evaluate])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                      )}
     "Evaluate"]

    [:a {:class [:pg-cell-actions :lg:inline]
         :on-click #(do
                      (dispatch [:segment/clear])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                      )}
     "Clear Output"]]

   ;; Move Segment around.
   [:div.text-lg
    [:a {:class    [:pg-cell-move :lg:inline-block :lg:mt-0]
         :on-click #(dispatch [:notebook/move :up])}
     [:i.fas.fa-arrow-circle-up]]
    [:a {:class    [:pg-cell-move :lg:inline-block :lg:mt-0]
         :on-click #(dispatch [:notebook/move :down])}
     [:i.fas.fa-arrow-circle-down]]]])
