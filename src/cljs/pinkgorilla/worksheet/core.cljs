(ns pinkgorilla.worksheet.core
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]

   [pinkgorilla.output.core :refer [output-fn]]
   
   [pinkgorilla.worksheet.code-segment :refer [code-segment]]
   [pinkgorilla.worksheet.code-segment-view :refer [code-segment-view]]
   [pinkgorilla.worksheet.free-segment :refer [free-segment]]
   [pinkgorilla.worksheet.free-segment-view :refer [free-segment-view]]
   [pinkgorilla.worksheet.free-output-view :refer [free-output-view]]
   [pinkgorilla.worksheet.free-output :refer [free-output]]
   [pinkgorilla.worksheet.helper :refer [init-cm! focus-active-segment error-text console-text exception]]
   
   ))


(defn worksheet
  [read-write editor-options]
  (let [worksheet (subscribe [:worksheet])]
    (fn [read-write editor-options]
      (let [segment-order (:segment-order @worksheet)
            segments (:segments @worksheet)]
        [:div.WorkSheet {}
         [:div {:class "segment container-segment"}
          [:div.container-children
           (for [seg-id segment-order]
             (do
               (let [segment (seg-id segments)]
                 (if read-write
                   (if (= :code (:type segment))
                     ^{:key seg-id} [code-segment segment editor-options]
                     ^{:key seg-id} [free-segment segment editor-options])
                   (if (= :code (:type segment))
                     ^{:key seg-id} [code-segment-view segment]
                     ^{:key seg-id} [free-segment-view segment])))))]]]))))