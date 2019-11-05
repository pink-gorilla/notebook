(ns pinkgorilla.worksheet.free-segment
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]

   [pinkgorilla.output.core :refer [output-fn]]
   [pinkgorilla.worksheet.free-output :refer [free-output]]
   [pinkgorilla.worksheet.helper :refer [init-cm! focus-active-segment error-text console-text exception]]))


(defn free-segment
  [seg-data editor-options]
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])
        footer ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
     {:display-name         "markup-segment"
      :component-did-update #(focus-active-segment %1 @is-active)
      :reagent-render       (fn [seg-data]
                              (let [free-value (:content @segment)
                                    div-kw (keyword (str "div#" (name seg-id)))
                                    class (str "segment free"
                                               (if @is-active
                                                 " selected"
                                                 ""))
                                    other-children [[free-output @is-active seg-id free-value editor-options]
                                                    footer]]
                                (apply conj
                                       [div-kw {:class    class
                                                :on-click #(dispatch [:worksheet:segment-clicked seg-id])}]
                                       (filter some? other-children))))})))
