(ns pinkgorilla.notebook-ui.datafy.views
  (:require
   [taoensso.timbre :as timbre :refer-macros [info]]
   [cljs.tagged-literals]
   [re-frame.core :refer [subscribe dispatch]]))

(defn Pane [{:keys [title id controls children]}]
  [:div {:id id
         :class "punk__pane__container"}
   [:div {:class "punk__pane__body-container"}
    [:div {:class "punk__pane__titlebar"}
     [:span title]]
    [:div {:class "punk__pane__body"}
     children]]
   (when controls
     [:div {:class "punk__pane__bottom-controls"}
      controls])])

; cols [kw-name pick display-comp]
; data - map / vec / set

(defn Table [{:keys [cols data on-entry-click] :as props}]
  (let [key-fn (-> cols first second)]
    [:div (dissoc props :data :cols :on-next :on-entry-click)
     [:div {:class "punk__table__top-labels"}
      (for [[col-name _ display-comp] cols]
        (conj display-comp (name col-name)))]
     (for [d data]
       [:div {:key (key-fn d)
              :class "punk__table__item"
              :on-click #(on-entry-click (key-fn d) d)}
        (for [[_ pick display-comp] cols]
          (conj display-comp (prn-str (pick d))))])]))

(defn MapView [{:keys [data nav] :as props}]
  [:div
   [:p "MapView"]
   [Table (merge props
                 {:cols [[:key first [:div {:style {:flex 1 :overflow "auto"}}]]
                         [:value second [:div {:style {:flex 3 :overflow "auto"}}]]]
                  :on-entry-click (fn [key]
                                    (let [val (get data key)]
                                      (info "mapview nav: " key  "val: " val) ; " data: " data
                                      (nav data key val)))
                  :data data}
                   ;; remove nav from props
                 {:nav nil})]])

(defn CollView [{:keys [data nav] :as props}]
  [Table (merge props
                {:cols [[:idx first [:div {:style {:flex 1 :overflow "auto"}}]]
                        [:value second [:div {:style {:flex 11 :overflow "auto"}}]]]
                 :on-entry-click (fn [key]
                                   (let [val (nth data key)]
                                     (info "colview nav key: " key "val: " val)  ; data:" data
                                     (nav data key val))) ; get makes problems on lists
                 :data (map-indexed vector data)}
                {:nav nil})])

(defn SetView [{:keys [data nav] :as props}]
  [Table (merge props
                {:cols [[:value identity [:div {:overflow "auto"}]]]
                 :on-entry-click (fn [v] (nav data nil v))
                 :data data}
                {:nav nil})])

(defn EdnView [{:keys [data nav] :as props}]
  [:div [:code {:on-click  #(nav data nil data)}

         (prn-str data)]])

(def views [{:id :punk.view/nil
             :match nil?
             :view nil}

            {:id :punk.view/map
             :match map?
             :view #'MapView}

            {:id :punk.view/set
             :match set?
             :view #'SetView}

            {:id :punk.view/coll
             :match (every-pred
                     coll?
                     (comp not map?))
             :view #'CollView}

            {:id :punk.view/edn
             :match any?
             :view #'EdnView}])

(def demo-table-coll
  [Table {:cols [[:idx first {:flex 1}]
                 [:value second {:flex 3}]]
          :data (map-indexed vector [1 2 3 4])}])

(def demo-table-map
  [Table {:cols [[:key (comp str first) {:flex 1}]
                 [:value (comp prn-str second) {:flex 3}]]
          :data {:foo "bar"
                 :baz #{1 2 3}}}])

(def demo-table-multiple-things
  [Table {:cols [[:key (comp str first) {:flex 1}]
                 [:value (comp prn-str second) {:flex 3}]
                 [:meta (comp prn-str meta second) {:flex 3}]]
          :data {:foo (with-meta {:asdf "jkl"} {:punk/tag 'cljs.core/Map})
                 :baz (with-meta #{1 2 3} {:punk/tag 'cljs.core/Set})}}])

(defn select-view [view views evt]
  [:div
   [:select {:value (str (:id view))
             :on-change #(dispatch [evt (keyword (subs (.. % -target -value) 1))])}
    (for [vid (map (comp str :id) views)]
      [:option {:key vid} vid])]])

(defn Next [{:keys [view views current]}]
  [:div
   [Pane {:title "Next"
          :controls [select-view view views :punk.ui.browser/select-next-view]}]
   (info "next view: " view)
   (when (nil? (:view view)) [:p "error - next view is nil."])
   (when (and current view (:view view))
     [(:view view)
      {:data (:value current)
       :id "punk__next"
       :nav #(dispatch [:punk.ui.browser/nav-to-next])}])])

(defn Breadcrumbs [{:keys [items on-click]}]
  [:<>
   (map-indexed
    (fn [i x]
      [:a {:href "#"
           :on-click #(do (.preventDefault %)
                          (on-click (+ i 1)))
           :class "punk__breadcrumb"} (str x)])
    (drop-last items))
   (when (seq items)
     [:span {:class ["punk__breadcrumb" "punk__breadcrumb_last"]} (str (last items))])])

(defn Current [{:keys [history view views current]}]
  [:div
   [Pane
    {:title "Current"
     :id "punk__current"
     :controls [:div
                [select-view view views :punk.ui.browser/select-current-view]
                [:button {:type "button"
                          :id "punk__current__back-button"
                          :disabled (empty? history)
                          :on-click #(dispatch [:punk.ui.browser/history-back])} "<"]
                [Breadcrumbs
                 {:items (map :nav-key history)
                  :on-click #(dispatch [:punk.ui.browser/history-nth %])}]]}]

   (when (and current view)
     [(:view view)
      {:data (:value current)
       :nav #(dispatch [:punk.ui.browser/preview (:idx current) %2 %3])}])])

(defn match-views [views data]
  (filter #((:match %) data) views))

(defn browser []
  (let [state @(subscribe [:punk])
        entries (reverse (map-indexed vector (:entries state)))
        current-views (-> views
                          (match-views (get-in state [:current :value])))
        current-view (if (:current.view/selected state)
                       (first (filter #(= (:id %) (:current.view/selected state)) current-views))
                       (first current-views))
        next-views (-> views
                       (match-views (get-in state [:next :value])))
        next-view (if (:next.view/selected state)
                    (first (filter #(= (:id %) (:next.view/selected state)) next-views))
                    (first next-views))]
    [:div
     [Next {:view next-view
            :views next-views
            :current (:next state)}]

     [:p (str "current idx: "
              (get-in state [:current :idx])
              " meta: ")
      (get-in state [:current :meta])]

     [Current {:view current-view
               :views current-views
               :current (:current state)
               :history (:history state)}]

     [Pane {:title "Entries" :id "punk__entries"
            :children
            [Table {:cols [[:id first [:div {:class "punk__entry-column__id"}]]
                           [:value (comp :value second) [:div {:class "punk__entry-column__value"}]]
                           ; [:meta (comp :meta second) {:flex 5}]
                           ]
                    :on-entry-click (fn [_ entry]
                                      (dispatch [:punk.ui.browser/view-entry (second entry)]))
                    :data entries}]}]]))

