(ns pinkgorilla.notebook-ui.nrepl.views.panel
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [dispatch]]))

(defn session-panel [session-id sessions]
  (if sessions
    [:div [:p.text-large.text-blue-700.text-bold.mt-5 "NRepl Sessions " (count sessions) " id: " session-id]
     (into [:div.grid.grid-cols-4.gap-1.w-full]
           (map (fn [s] [:p {:class (if (= session-id s) "text-red-700" "")}
                         (str s)]) sessions))]
    [:p.text-large.text-red-700.text-bold "no session data!"]))

(defn middleware-panel [mws]
  (if mws
    [:div [:p.text-large.text-blue-700.text-bold.mt-5 "NRepl Middlewares " (count mws)]
     (into [:div.grid.grid-cols-4.gap-1.w-full]
           (map (fn [s] [:p ; {:class "w-1/5"} 
                         (str s)]) (sort mws)))]
    [:p.text-large.text-red-700.text-bold "no middleware data!"]))

(defn ops-panel [ops]
  [:div
   [:p.text-large.text-blue-700.text-bold.mt-5 "NRepl Operations"]
   (into [:div.grid.grid-cols-4.gap-1.w-full]
         (map (fn [o] [:p ; {:class "w-1/5"} 
                       o]) (sort ops)))])

(defn v [title version]
  [:p
   [:span.w-16.inline-block title]
   [:span version]])

(defn desc-panel [description]
  (if description
    (let [nrepl-version (get-in description [:versions :nrepl :version-string])
          clj-version (get-in description [:versions :clojure :version-string])
          java-version (get-in description [:versions :java :version-string])
          cider-version (get-in description [:aux :cider-version :version-string])
          ops (get-in description [:ops])
          ops-list (keys ops)]
      [:div
       [:p.text-large.text-blue-700.text-bold.mt-5 "NRepl Versions"]
       [v "nREPL" nrepl-version]
       [v "Clojure" clj-version]
       [v "Java" java-version]
       [v "Cider" cider-version] ; nrepl 0.8 does not provide cider info ???
       [ops-panel ops-list]])
    [:div "No desc rcvd!"]))

(defn sniffer-panel [sniffer-status]
  [:div
   [:p.text-large.text-blue-700.text-bold.mt-5 "NRepl Sniffer status"]
   [:div.grid.grid-cols-4.gap-1.w-full
    (if sniffer-status
      (let [{:keys [session-id-sink session-id-source]} sniffer-status]
        [:<>
         [:p (str "sink: " (or session-id-sink "no sink"))]
         [:p (str "source: " (or session-id-source "no source"))]])
      [:p "no sniffer status!"])]])

(defn info-panel [session-id describe sessions mws sniffer-status]
  [:div
   [:button.bg-green-400 {;:type "button"
                          :on-click #(dispatch [:nrepl/info-get])} "get info"]
   [desc-panel describe]
   [middleware-panel mws]
   [session-panel session-id sessions]
   [sniffer-panel sniffer-status]])
