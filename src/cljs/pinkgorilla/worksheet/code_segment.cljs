(ns pinkgorilla.worksheet.code-segment
  (:require
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe dispatch]]
   [re-catch.core :as rc]
   [pinkgorilla.output.core :refer [output-fn]]
   [pinkgorilla.worksheet.helper :refer [init-cm! focus-active-segment error-text console-text exception]]))


(defn output-view [seg-id segment]
  (try
    (if-let [value-output (not-empty (:value-response @segment))]
      (let [output-value (output-fn value-output)
            component ^{:key :value-response} [:div.output>pre [output-value value-output seg-id]]    
            ]
        (println "returning reagent: " component)
        component
        
        ))
    (catch Exception e [:p (str "exception rendering cell output: " (.getMessage e))])))

(defn code-segment-unsafe
  [seg-data editor-options]
  ;; TODO: active <=> selected, executing <=> running
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])
        is-active (subscribe [:is-active-query seg-id])
        is-queued (subscribe [:is-queued-query seg-id])
        footer-comp ^{:key :segment-footer} [:div.segment-footer]]
    (reagent/create-class
     {:component-did-mount  (fn [this]
                              ((partial init-cm!
                                        seg-id
                                        (get-in seg-data [:content :type])
                                        editor-options) this)
                              (focus-active-segment this
                                                    @is-active))
       ;; :component-will-mount #()
      :display-name         "code-segment"
      :component-did-update #(focus-active-segment %1 @is-active)
      :reagent-render       (fn [seg-data]
                              (let [main-component
                                    ^{:key :segment-main} [:div.segment-main
                                                           [:textarea {:class     "codeTextArea mousetrap"
                                                                       :value     (get-in @segment [:content :value])
                                                                       :read-only true}]]
                                    error-comp (if-let [err-text (:error-text @segment)]
                                                 ^{:key :error-text} [error-text err-text])
                                    ex-comp (if-let [ex (:exception @segment)]
                                              ^{:key :exception} [exception ex])
                                    console-comp (if-let [cons-text (not-empty (:console-response @segment))]
                                                   ^{:key :console-response} [console-text cons-text])
                                    output-comp (output-view seg-id segment)
                                    div-kw (keyword (str "div#" (name seg-id))) ;; Aid with debugging
                                    class (str "segment code"
                                               (if @is-active
                                                 " selected"
                                                 "")
                                               (if @is-queued
                                                 " running"
                                                 ""))
                                    other-children [main-component
                                                    error-comp
                                                    ex-comp
                                                    console-comp
                                                    output-comp
                                                    footer-comp]]
                                (apply conj [div-kw
                                             {:class    class
                                              :on-click #(dispatch [:worksheet:segment-clicked seg-id])}]
                                       (filter some? other-children))))})))

(defn code-segment [seg-data editor-options]
  [rc/catch
   [code-segment-unsafe seg-data editor-options]])




