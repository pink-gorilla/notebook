(ns pinkgorilla.worksheet.free-output
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]

   [pinkgorilla.output.mathjax :refer [queue-mathjax-rendering]]
   [pinkgorilla.output.core :refer [output-fn]]
   [pinkgorilla.worksheet.helper :refer [init-cm! focus-active-segment error-text console-text exception colorize-cm!]]))



(defn free-output
  [active seg-id content editor-options]
  (let [prev-uuid (uuid/uuid-string (uuid/make-random-uuid))
        prev-div-kw (keyword (str "div#" prev-uuid))
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])]
    (reagent/create-class
     {:component-did-mount  (fn [this]
                              (if active
                                (do
                                  ((partial init-cm!
                                            seg-id
                                            (get-in content [:type])
                                            editor-options) this)
                                  (focus-active-segment this true))))
      :display-name         "free-output"
      :component-did-update (fn [this]
                              (if @is-active
                                (do
                                  ((partial init-cm!
                                            seg-id
                                            (get-in content [:type])
                                            editor-options) this)
                                  (focus-active-segment this true))
                                (queue-mathjax-rendering prev-uuid)
                                #_(let [el (gdom/getElement prev-uuid)])))

       ;; if ("MathJax" in window) MathJax.Hub.Queue(["Typeset", MathJax.Hub, $(element).attr('id')]);
       ;; :reagent-render      nil
      :reagent-render       (fn [active seg-id content]
                              (if active
                                [:div.segment-main
                                 [:div.free-markup
                                  [:textarea {:value     (get-in @segment [:content :value])
                                              :read-only true}]]]
                                [prev-div-kw {:class                   "free-preview"
                                              :dangerouslySetInnerHTML {:__html (js/marked (:value content))}}]))})))


