(ns pinkgorilla.worksheet.code-cell-menu
  (:require
   [pinkgorilla.prefs :as prefs]
   [re-frame.core :refer [dispatch]]))

(defn cell-menu [segment]
  [:div {:class [:font-sans :flex :flex-col :text-center :sm:flex-row :sm:text-left :sm:justify-between :px-6 :bg-white :sm:items-baseline :w-full]}

   ;; Kernel (clj/cljs)
   ;; TODO: Not yet ready for master
   (prefs/if-cljs-kernel
    [:div.mb-1
     [:a {:class [:text-lg :p-1 :pg-kernel-toggle]
          :on-click #(dispatch [:app:kernel-toggle])}
      (:kernel @segment)]]
    nil)

   [:div.mt-1.mb-1.h-8
   ;[:p {:class "text-lg no-underline text-grey-darkest hover:text-blue-dark ml-2"} "One" ]

    [:a {:class [:pg-cell-actions :lg:inline]
         :on-click #(do
                      (dispatch [:worksheet:evaluate])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                      )}
     "Evaluate"]

    [:a {:class [:pg-cell-actions :lg:inline]
         :on-click #(do
                      (dispatch [:worksheet:clear-output])
                      ;(rf/dispatch [::events/set-navbar-menu-active? false])
                      )}
     "Clear Output"]]

   ;; Move Segment around.
   [:div.text-lg
    [:a {:class    [:pg-cell-move :lg:inline-block :lg:mt-0]
         :on-click #(dispatch [:worksheet:moveUp])}
     [:i.fas.fa-arrow-circle-up]]
    [:a {:class    [:pg-cell-move :lg:inline-block :lg:mt-0]
         :on-click #(dispatch [:worksheet:moveDown])}
     [:i.fas.fa-arrow-circle-down]]]])
