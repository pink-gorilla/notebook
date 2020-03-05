(ns pinkgorilla.worksheet.free-output
  (:require
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe]]
   [dommy.core :as dom :refer-macros [sel1]]
   [pinkgorilla.output.mathjax :refer [queue-mathjax-rendering]]
   [pinkgorilla.worksheet.helper :refer [init-cm! focus-active-segment]]))

(defn free-output
  [active seg-id content editor-options]
  (let [prev-uuid (uuid/uuid-string (uuid/make-random-uuid))
        prev-div-kw (keyword (str "div#" prev-uuid))
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])
        init-fn (partial init-cm!
                         seg-id
                         (get-in content [:type])
                         editor-options)]
    (reagent/create-class
     {:display-name         "free-output"
      :component-did-mount  (fn [this]
                              (if active
                                (do
                                  (init-fn this)
                                  (focus-active-segment this true))
                                (queue-mathjax-rendering (name seg-id))))
      :component-did-update (fn [this]
                              (if @is-active
                                (do
                                  (let [text-area (-> (reagent/dom-node this)
                                                      (sel1 :textarea))]
                                    (when-not (= "none" (.. text-area -style -display))
                                      (init-fn this)))
                                  (focus-active-segment this true))
                                (queue-mathjax-rendering prev-uuid)
                                #_(let [el (gdom/getElement prev-uuid)])))

       ;; if ("MathJax" in window) MathJax.Hub.Queue(["Typeset", MathJax.Hub, $(element).attr('id')]);
       ;; :reagent-render      nil
      :reagent-render       (fn [active _ content] ; seg-id
                              (if active
                                [:div.segment-main
                                 [:div.free-markup
                                  [:textarea {:value     (get-in @segment [:content :value])
                                              :read-only true}]]]
                                [prev-div-kw {:class                   "free-preview"
                                              :dangerouslySetInnerHTML {:__html (js/marked (:value content))}}]))})))
