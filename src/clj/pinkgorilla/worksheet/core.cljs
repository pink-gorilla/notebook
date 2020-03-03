(ns pinkgorilla.worksheet.core
  (:require
   [re-frame.core :refer [subscribe]]
   [pinkgorilla.worksheet.code-segment :refer [code-segment-edit code-segment-view]] ; code   
   [pinkgorilla.worksheet.md-segment :refer [md-segment-edit md-segment-view]] ; markdown
   ))

(defn worksheet
  [_ _] ;;read-write editor-options
  (let [worksheet (subscribe [:worksheet])]
    (fn [read-write editor-options]
      (let [segment-order (:segment-order @worksheet)
            segments (:segments @worksheet)]
        [:div.WorkSheet {}
         [:div {:class "segment container-segment"}
          [:div.container-children
           (for [seg-id segment-order]
             (let [segment (seg-id segments)]
               (if read-write
                 (if (= :code (:type segment))
                   ^{:key seg-id} [code-segment-edit segment editor-options]
                   ^{:key seg-id} [md-segment-edit segment])
                 (if (= :code (:type segment))
                   ^{:key seg-id} [code-segment-view segment]
                   ^{:key seg-id} [md-segment-view segment]))))]]]))))
