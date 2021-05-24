(ns demo.views.main
  (:require
   [taoensso.timbre :as timbre :refer [info]]

     [re-frame.core :refer [dispatch]]
  ))


(defn action [{:keys [on-click href]} text]
  [:div.w-48.h-12.p-5.border-2.border-solid.border-gray-500.rounded.text-center.text-lg.cursor-pointer.bg-pink-100.hover:bg-pink-400
   {:on-click on-click
    :href href}
   (if href
     [:a {:href href} text]
     text)])


(defn add-new-notebooks [nr]
  (doall (for [i (range nr)]
    (do
    (info "creating new document " i )
    (dispatch [:document/new])
    )
  )))


(defn main-view []
  [:div
   [:h1 "notebook-ui demo"]
   [action {:href "/explorer"} "explorer"]
   [action {:on-click #(add-new-notebooks 5)} "5 new notebooks"]

   
   ])