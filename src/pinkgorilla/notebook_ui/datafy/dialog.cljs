(ns pinkgorilla.notebook-ui.datafy.dialog
  (:require
   [taoensso.timbre :as timbre :refer-macros [info]]
   [reagent.core :as r]
   [re-frame.core :refer [subscribe dispatch reg-event-db reg-sub]]
   [pinkgorilla.notebook-ui.datafy.views :refer [browser]]))

#_(defn sub-browse [{:keys [id val]} K]
    (let [s (get val K)]
      (when (map? s)
        (into [:div]
              (map (fn [[k v]]
                     [:a.m-2 {:on-click #(dispatch [:datafy/nav id K k])}
                      [:span (pr-str k)]])
                   s)))))

#_(defn browse [{:keys [val id] :as datafy}]
    (when (map? val)
      (into [:div]
            (map (fn [[k v]]
                   [:div.bg-green-300
                    (pr-str k)
                    [sub-browse datafy k]
                  ;[:a.m-2 {:on-click #(dispatch [:datafy/nav id k nil])}]
                  ;[:span.m-2 (pr-str v)]
                    ])
                 val))))

#_(defn datafy-dialog-old [];{:keys [id val meta] :as datafy}]
  ;(dispatch [:datafy/nav id nil nil])
    (let [datafy (subscribe [:datafy/data])
          {:keys [id val meta]} @datafy]
      [:div.bg-white
       [:p {:class "text-center text-3xl"} "Datafy"]
       [:p "id: " id]
       [:p "meta: " meta]
     ;[:p "val" (pr-str val)]
       [browse datafy]]))

(defn datafy-dialog [];{:keys [id val meta] :as datafy}]
  ;(dispatch [:datafy/nav id nil nil])
 ; (let [datafy (subscribe [:datafy/data])
 ;       {:keys [id val meta]} @datafy]
  [:div.bg-white
   [browser]])