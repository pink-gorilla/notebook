(ns pinkgorilla.worksheet.code-segment-view
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe]]

   [pinkgorilla.output.core :refer [output-fn]]
   [pinkgorilla.worksheet.helper :refer [error-text console-text exception colorize-cm!]]))

(defn code-segment-view
  [seg-data]
  ;; TODO: active <=> selected, executing <=> running
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        footer-comp ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
     {:component-did-mount (fn [this]
                             (colorize-cm! this))
      :display-name        "code-segment-view"
      :reagent-render      (fn [_]                  ;; repeat seg-data if we want to use it
                             (let [main-component
                                   ^{:key :segment-main} [:div.segment-main
                                                          [:pre {:class "static-code clojure"}
                                                           (get-in @segment [:content :value])]]
                                    ;; TODO : Factor out error, ex, console, output
                                   error-comp (if-let [err-text (:error-text @segment)]
                                                ^{:key :error-text} [error-text err-text])
                                   ex-comp (if-let [ex (:exception @segment)]
                                             ^{:key :exception} [exception ex])
                                   console-comp (if-let [cons-text (not-empty (:console-response @segment))]
                                                  ^{:key :console-response} [console-text cons-text])
                                   output-comp (if-let [value-output (not-empty (:value-response @segment))]
                                                 (let [output-value (output-fn value-output)]
                                                   ^{:key :value-response}
                                                   [:div.output>pre [output-value value-output seg-id]]))
                                   div-kw (keyword (str "div#" (name seg-id))) ;; Aid with debugging
                                   other-children [main-component
                                                   error-comp
                                                   ex-comp
                                                   console-comp
                                                   output-comp
                                                   footer-comp]]
                               (apply conj [div-kw {:class "segment code"}]
                                      (filter some? other-children))))})))
