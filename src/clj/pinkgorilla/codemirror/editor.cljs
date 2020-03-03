(ns pinkgorilla.codemirror.editor
  (:require
   [reagent.dom]
   [reagent.core :as reagent]
   [re-frame.core :refer [subscribe]]
   [pinkgorilla.codemirror.core :refer [create-editor!]]))

(defn create-cm!
  [segment editor-options text-area]
  (let [segment-id (:id segment)
        content-type (get-in segment [:content :type])]
    (create-editor! text-area
                    :content-type content-type
                    :segment-id segment-id
                    :opts editor-options)))

(defn colorize-cm!
  [pre]
  (.colorize js/CodeMirror #js [pre] "text/x-clojure"))

(defn focus-cm!
  [cm active?]
  (when @active?
    (when-let [cm @cm]
      (.focus cm))))

(defn editor
  [segment read-only? editor-options]
  (let [segment-id (:id @segment)
        active? (subscribe [:is-active-query segment-id])
        cm (atom nil) ; codemirror instance that has been created
        textarea (atom nil)
        pre (atom nil)]
    (reagent/create-class
     {:display-name "editor"
      :component-did-mount  (fn [_]
                              (if read-only?
                                (colorize-cm! @pre)
                                (when-not @cm
                                  (reset! cm (create-cm! @segment editor-options @textarea))
                                  (focus-cm! cm active?))))
      :component-did-update (fn []
                              (when (not read-only?)
                                (focus-cm! cm active?)))
      :reagent-render (fn [_]
                        [:div
                         (if read-only?
                           [:pre {:class "static-code clojure"
                                  :ref #(when % (reset! pre %))}]
                           [:textarea {:class "codeTextArea mousetrap"
                                       :value (get-in @segment [:content :value])
                                       :read-only true
                                       :ref #(when % (reset! textarea %))}])])})))
