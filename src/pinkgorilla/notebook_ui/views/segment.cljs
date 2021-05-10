(ns pinkgorilla.notebook-ui.views.segment
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.notebook-ui.eval-result.eval-result :refer [eval-result-view]]
   [pinkgorilla.notebook-ui.codemirror.codemirror-edit  :refer [codemirror-edit]]
   [pinkgorilla.notebook-ui.prosemirror.md :refer [md-segment-view]]
   [pinkgorilla.notebook-ui.prosemirror.editor :refer [prosemirror]]))

;; input

(defn segment-input-md [{:keys [segment-active?] :as cm-opt}
                        {:keys [id] :as eval-result}]
  [:div.prose
   [prosemirror id segment-active?]])

(defn segment-input-code [{:keys [segment-active?] :as cm-opt} eval-result]
  [:div {:style {:position "relative"}} ; container for kernel absolute position
   ; kernel - one color for both dark and light themes.
   (when-let [kernel (:kernel eval-result)] ; snippets might not define kernel
     [:span.pr-2.text-right.text-blue-600.tracking-wide.font-bold.border.border-blue-300.rounded
      {:on-click #(dispatch [:segment-active/kernel-toggle])
       :style {:position "absolute"
               :z-index 200 ; dialog is 1040 (we have to be lower)
               :top "2px" ; not too big, so it works for single-row code segments
               :right "10px"
               :width "50px"
               :height "22px"}} kernel])
   (if segment-active?
     [codemirror-edit (:id eval-result) cm-opt]
     [codemirror-edit (:id eval-result) cm-opt])])

(defn segment-input [{:keys [id code md] :as eval-result}]
  (let [settings (subscribe [:settings])
        segment-active (subscribe [:notebook/segment-active])
        segment-queued (subscribe [:segment/queued? id])
        cm-md-edit? (subscribe [:notebook/edit?])]
    (fn [{:keys [id code md] :as eval-result}]
      (let [active? (= (:id @segment-active) id)
            cm-md-edit? @cm-md-edit?
            queued? @segment-queued
            options  {:segment-active? active?}
            full? (= (:layout @settings) :single)]
        [:div.text-left.bg-gray-100 ; .border-solid
         {:id id
          :on-click #(do
                       (dispatch [:notebook/move :to id])
                       (dispatch [:notebook/set-cm-md-edit true]))
          :class (str (if queued?
                        "border border-solid border-blue-600"
                        (if active?
                          (if cm-md-edit? "border border-solid border-red-600"
                              "border border-solid border-gray-600")
                          ""))
                      (if full? " h-full" ""))}

         (cond
           code [segment-input-code options eval-result]
           md   [segment-input-md options eval-result])]))))

;; output

(defn segment-output [{:keys [code md] :as eval-result}]
  [:div
   (when md
     [md-segment-view eval-result])
   (when code
     [eval-result-view eval-result])])

(defn segment-output-no-md [{:keys [code md] :as eval-result}]
  [:div
   (when md
     ;[md-segment-view eval-result]
     [:div.no-md])
   (when code
     [eval-result-view eval-result])])





