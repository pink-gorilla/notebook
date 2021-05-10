(ns pinkgorilla.notebook-ui.completion.view
  (:require
   [cljs.reader]
   [cljs.tools.reader]
   [reagent.core :as r]
   [reagent.dom :as rd]
   [reepl.helpers :as helpers]
   [pinkie.pinkie :refer-macros [register-component]]))

; from: https://github.com/jaredly/reepl/blob/master/src/reepl/completions.cljs

(def styles
  {:completion-container {;:position :relative
                          :font-size 12}
   :completion-list {:flex-direction :row
                     :overflow :hidden
                     :height 20}
   :completion-empty {:color "#ccc"
                      ;;:font-weight :bold
                      :padding "3px 10px"}

   :completion-show-all {;:position :absolute
                         :top 0
                         :left 0
                         :right 0
                         :z-index 1000
                         :flex-direction :row
                         :background-color "#eef"
                         :flex-wrap :wrap}
   :completion-item {;; :cursor :pointer TODO make these clickable
                     :padding "3px 5px 3px"}
   :completion-selected {:background-color "#eee"}
   :completion-active {:background-color "#aaa"}})

(def view (partial helpers/view styles))
(def text (partial helpers/text styles))
(def button (partial helpers/button styles))

(def canScrollIfNeeded
  (not (nil? (.-scrollIntoViewIfNeeded js/document.body))))

(defn completion-item [text is-selected is-active set-active]
  (r/create-class
   {:component-did-update
    (fn [this [_ _ old-is-selected]]
      (let [[_ _ is-selected] (r/argv this)]
        (if (and (not old-is-selected)
                 is-selected)
          (if canScrollIfNeeded
            (.scrollIntoViewIfNeeded (rd/dom-node this) false)
            (.scrollIntoView (rd/dom-node this))))))
    :reagent-render
    (fn [text is-selected is-active set-active]
      [view {:on-click set-active
             :style [:completion-item
                     (and is-selected
                          (if is-active
                            :completion-active
                            :completion-selected))]}
       text])}))

(defn completion-list [{:keys [pos list active show-all set-active]}]
  (let [items (map-indexed
               (fn [i v]
                 [completion-item
                  (:candidate v) ; text
                  (= v active) ;(= i pos)   ; selected
                  (= v active) ; active
                  (partial set-active v)]) ; on active changed
               list)]
    [view :completion-container
     (if (empty? items)
       [view :completion-empty "This is where completions show up"]
       (if show-all
         (into [view :completion-show-all] items)
         (into
          [view :completion-list]
          items)))]))

#_(defn completion-view [o d]
    (let [x (r/atom {:pos 1
                     :list o
                     :active 0
                     :show-all true})
          _ (swap! x assoc :set-active (fn [i & a]
                                         (println "set active: " i)
                                         (swap! x assoc :active i)
                                         (swap! x assoc :pos i)))]
      (fn [o d]
        [:div {:style {:font-family "monospace"
                       :flex 1
                       :display :flex
                       :white-space "pre-wrap"}}
         [completion-list @x]
         [docs-view d]])))
