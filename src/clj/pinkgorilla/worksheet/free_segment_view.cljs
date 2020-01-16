(ns pinkgorilla.worksheet.free-segment-view
  (:require
   [reagent.core :as reagent]
   [pinkgorilla.worksheet.free-output-view :refer [free-output-view]]))

(defn free-segment-view
  [seg-data]
  (let [seg-id (:id seg-data)
        footer ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
     {:display-name   "markup-segment-view"
      :reagent-render (fn [seg-data]
                        (let [free-value (:content seg-data)
                               ;; Aid with debugging
                              div-kw (keyword (str "div#" (name seg-id)))
                              other-children [[free-output-view seg-id free-value]
                                              footer]]
                          (apply conj
                                 [div-kw {:class "segment free"}]
                                 (filter some? other-children))))})))
