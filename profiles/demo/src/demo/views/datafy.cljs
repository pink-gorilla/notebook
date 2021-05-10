(ns demo.views.datafy
  (:require
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.notebook-ui.datafy.views :refer [browser]]))


(defn action [{:keys [on-click href]} text]
  [:div.w-48.h-8.p-5.border-2.border-solid.border-gray-500.rounded.text-center.text-lg.cursor-pointer.bg-pink-100.hover:bg-pink-400
   {:on-click on-click
    :href href}
   (if href
     [:a {:href href} text]
     text)])



(defn datafy-demo []
 [:div.bg-red-500
  [:h1 "datafy demo (click on the links)"]
  
  [:div.bg-green-300
   [:p "eval in nrepl:"]
   [action {:on-click #(dispatch [:datafy/tap "{:a 1 :b 777}"])}
    "a map"]
   [action {:on-click #(dispatch [:datafy/tap "*ns*"])}
    "*ns*"]
   [action {:on-click #(dispatch [:datafy/tap "(in-ns 'clojure.pprint)"])}
    "namespace pprint"]
   [action {:on-click #(dispatch [:datafy/tap "(picasso.datafy.file/make-path \"/\")"])}
    "path"]]
  [browser]])


