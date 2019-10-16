(ns gorilla-repl.worksheet.free-output-view
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]

   [gorilla-repl.output.core :refer [output-fn]]
   [gorilla-repl.worksheet.helper :refer [init-cm! focus-active-segment error-text console-text exception colorize-cm!]]))


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


