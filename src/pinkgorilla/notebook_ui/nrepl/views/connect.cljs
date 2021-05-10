(ns pinkgorilla.notebook-ui.nrepl.views.connect
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [subscribe dispatch]]))

(defn connect-ui []
  (let [nrepl (subscribe [:nrepl])
        ws-url (r/atom (:ws-url @nrepl))] ;ws-url allows user o change url
    (fn []
      (let [{:keys [connected?]} @nrepl]
        (if connected?
          [:p.text-green-800 "Connected to: " @ws-url]
          [:div.border.border-red-500
           [:h1.text-xl "connect to nrepl relay"]
           [:span "NRepl Relay url:"]
           [:input.ml-5.mr-5 {:style {:min-width "300px"}
                              :value @ws-url
                              :on-change (fn [evt]
                                           (let [v (-> evt .-target .-value)]
                                             (reset! ws-url v)))}]
           [:button.bg-green-400 {;:type "button"
                                  :on-click #(dispatch [:nrepl/connect-to @ws-url])} "connect"]])))))

