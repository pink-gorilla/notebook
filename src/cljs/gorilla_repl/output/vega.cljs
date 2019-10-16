(ns gorilla-repl.output.vega
  (:require 
   [cljs-uuid-utils.core :as uuid]
   [reagent.core :as reagent :refer [atom]]
   [re-frame.core :refer [subscribe dispatch dispatch-sync]]
   
   [gorilla-repl.output.hack :refer [value-wrap]]
   ))


;; see renderer.js
(defn output-vega
  [output seg-id]
  ;; TODO: Check vega error handling
  ;; for some reason, Vega (1.3.3?) will sometimes try and pop up an alert if there's an error, which is not a
  ;; great user experience. Here we patch the error handling function to re-route any generated message
  ;; to the segment.
  ;; vg.error = function (msg) { errorCallback("Vega error (js): " + msg); };
  ;; (set! (.-doNothing cm-commands) #())
  (let [uuid (uuid/uuid-string (uuid/make-random-uuid))
        span-kw (keyword (str "span#" uuid))]
    (reagent/create-class
     {:component-did-mount (fn [this]
                             (try                          ;; vg.parse.spec(data.content, function (chart) {
                                ;; (js-debugger)
                                ;; (print (.stringify js/JSON (clj->js (:content output))))
                               (.spec (.-parse js/vg)
                                      (clj->js (:content output))
                                      (fn [chart]
                                        (let [s (str uuid)
                                               ;; TODO: Huh? WTF?
                                              el (js/document.getElementById s)
                                               ;; el (sel1 (keyword (str "#" uuid)))
                                               ;; el (by-id s)
                                              ]
                                          (-> (chart #js {:el el :renderer "svg"})
                                              .update))))              ;
                               (catch js/Object e
                                 (dispatch [:output-error seg-id e]))))
      :reagent-render      (fn []
                             [value-wrap (get output :value)
                              [span-kw {:class "vega-span"}]])})))