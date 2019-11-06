(ns pinkgorilla.worksheet.free-output-view
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent]))


(defn free-output-view
  [seg-id content]
  (let [prev-uuid (uuid/uuid-string (uuid/make-random-uuid))
        prev-div-kw (keyword (str "div#" prev-uuid))]
    (reagent/create-class
     {:display-name   "free-output-view"
      :reagent-render (fn [seg-id content]                 ;; repeat
                        [:div.segment-main
                         [:div.free-markup
                          [prev-div-kw {:class                   "free-preview"
                                        :dangerouslySetInnerHTML {:__html (js/marked (:value content))}}]]])})))


