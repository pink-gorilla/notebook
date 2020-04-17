(ns pinkgorilla.worksheet.code-segment
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.components.error :refer [error-boundary]]
   [pinkgorilla.worksheet.code-cell-menu :refer [cell-menu]]
   [pinkgorilla.output.core :refer [output-fn]]
   [pinkgorilla.codemirror.editor :refer [editor]]
   [pinkgorilla.worksheet.code-stacktrace :refer [stacktrace-table error-text console-text]]))

(defn output-view-unsafe [seg-id segment]
  (try
    (if-let [value-output (not-empty (:value-response @segment))]
      (let [output-value (output-fn value-output)
            component ^{:key :value-response} [:div.output>pre [output-value value-output seg-id]]]
        ;(println "returning reagent: " component)
        component))
    (catch js/Error e [:p (str "exception rendering cell output: " (. e -message))])))

(defn output-view [seg-id segment]
  [error-boundary
   [output-view-unsafe seg-id segment]])

(defn code-editor [read-only? seg-id segment editor-options]
    ;; TODO: active <=> selected, executing <=> running
  (let [active? (subscribe [:is-active-query seg-id])
        queued? (subscribe [:is-queued-query seg-id])]
    (fn []
      [:<>
       [:div {:id (name seg-id)
              :class (str "segment code"
                          (if @active? " selected" "")
                          (if @queued? " running" ""))
              :on-click #(dispatch [:worksheet:segment-clicked seg-id])}
      ; editor
        ^{:key :segment-main}
        [:div.segment-main
         [editor segment read-only? editor-options]]]
      ; menu
       (when @active? [cell-menu segment])])))

(defn code-segment-unsafe
  [read-only? seg-data editor-options]
  (let [seg-id (:id seg-data)
        segment (subscribe [:segment-query seg-id])]
    (fn []
      [:<>
       ; editor 
       [code-editor read-only? seg-id segment editor-options]
      ; error
       (if-let [err-text (:error-text @segment)]
         ^{:key :error-text}
         [error-text err-text])
      ; stacktrace
       (if-let [ex (:exception @segment)]
         ^{:key :exception}
         [stacktrace-table ex])
      ; console
       (if-let [cons-text (not-empty (:console-response @segment))]
         ^{:key :console-response}
         [console-text cons-text])
      ; output
       (output-view seg-id segment)
      ; footer      
       ^{:key :segment-footer}
       [:div.segment-footer]])))

(defn code-segment-edit
  "code-segment - editable"
  [seg-data editor-options]
  [error-boundary
   [code-segment-unsafe false seg-data editor-options]])

(defn code-segment-view
  "code-segment - view-only"
  [seg-data]
  [error-boundary
   [code-segment-unsafe true seg-data {}]])



