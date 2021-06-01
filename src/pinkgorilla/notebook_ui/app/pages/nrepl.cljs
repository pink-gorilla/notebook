(ns pinkgorilla.notebook-ui.app.pages.nrepl
  (:require
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [pinkgorilla.notebook-ui.nrepl.views.info-page :refer [nrepl-info]]
   [pinkgorilla.notebook-ui.app.site :as site]))

(defn nrepl-ip-port []
  (let [config (rf/subscribe [:webly/config])]
    (fn []
      (let [{:keys [bind port]} (get-in @config [:nrepl :server])
            enabled (get-in @config [:nrepl :enabled])]
        (if enabled
          [:span "ip:" bind " : " port]
          [:span "nrepl disabled"])))))

(defn sniffer-box []
  [:div.bg-yellow-300.mt-5.p-5
   [:h1.text-2xl "Eval Sniffer"]
   [:div.prose
    [:ul.list-discs
     [:li "Work in your favorite ide, and build a notebook with gorilla visualisations."]
     [:li "connect your ide to: " [nrepl-ip-port]]
     [:li "Eval :gorilla/on in the repl"]
     [:li "A new notebook appears in /explorer (in unsaved)"]
     [:li "In /nrepl/info you can see the sessions that are currently being sniffed."]
     [:li "To stop sniffing eval :gorilla/off in the repl"]]]])

(defmethod reagent-page :ui/nrepl [{:keys [route-params query-params handler] :as route}]
  [:<>
   [site/header]
   [:div
    [sniffer-box]
    [:h1 "nrepl info"]
    [nrepl-info]]])
