(ns pinkgorilla.dialog.palette
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]

   [dommy.core :as dom :refer-macros [sel1]]
   [goog.dom :as gdom]

   ))

(defn palette-dialog
  []
  (let [palette (subscribe [:palette])]
    (reagent/create-class
     {:display-name         "palette-dialog"
      :component-did-update (fn [this old-argv]
                               ;; TODO: Focus or not?
                              (let [el (reagent/dom-node this)]
                                (if (:show @palette)
                                  (-> el
                                      (sel1 :input)
                                      .focus))
                                (if-let [actEl (gdom/getElementByClass "highlight" el)]
                                  (if (.-scrollIntoViewIfNeeded actEl)
                                    (.scrollIntoViewIfNeeded actEl false)
                                    (.scrollIntoView actEl false)))))
      :reagent-render
      (fn []
        (let [cmd-items (:visible-items @palette)
              highlight (:highlight @palette)
              items (map-indexed (fn [i x]
                                   [(if (= i highlight)
                                      :div.palette-item.highlight>li
                                      :div.palette-item>li)
                                    {:on-click                #(dispatch-sync [:palette-action x])
                                     :dangerouslySetInnerHTML {:__html (:desc x)}}])
                                 cmd-items)
              ul (into [:ul] items)]
          [:div.PaletteDialog {:style (if (false? (:show @palette)) {:display "none"} {})}
           [:div.PaletteDialog.modal-overlay
            {:on-click #(dispatch [:palette-blur])}]
           [:div.PaletteDialog.modal
            [:h3 {:dangerouslySetInnerHTML {:__html (:label @palette)}}]
            [:div.modal-content
             [:input {:type        "text"
                      :value       (:filter @palette)
                      :on-change   #(dispatch [:palette-filter-changed (-> % .-target .-value)])
                      :on-key-down #(dispatch-sync [:palette-filter-keydown (.-which %)])
                                             ;; TODO  : on-blur kicks in before menu gets the click, but we want
                                             ;; :on-blur   #(dispatch [:palette-blur])
                                             ;; :on-mouse-down #(dispatch [:palette-blur])
                      :ref         "filterText"}]
             [:div.palette-items ul]]]]))})))


