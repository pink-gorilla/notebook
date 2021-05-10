(ns pinkgorilla.notebook-ui.editor.base
  (:require
   [taoensso.timbre :refer-macros [debug debugf info infof warn error]]
   [reagent.core :as r]
   [reagent.dom :as rd]))

; currently only usd by prosemirror
; idea is that codemirror uses this also.


(defn make-editor [{:keys [get-data
                           ;set-data 
                           editor-load-content
                           editor-create
                           editor-destroy
                           editor-focus]}]

  (fn [id active?]
    (let [editor (r/atom nil)
          active-a (r/atom active?)]
      (r/create-class
       {:component-did-mount
        (fn [this]
          (let [el (rd/dom-node this)
                content (get-data id)
                editor_ (editor-create id el content)]
            (reset! editor editor_)
            (when active?
              (println "md active: " id)
              (editor-focus @editor))))

        :component-will-unmount
        (fn [this]
          (debug "cm component-will-unmount")
          (editor-destroy @editor))

        :component-did-update
        (fn [this old-argv]
          (let [[_ id active?] (r/argv this)]
          ;(info "component-did-update: current buffer: " eval-result9
            (reset! active-a active?)
            (when active?
              (println "md active: " id)
              (editor-focus @editor))
            (editor-load-content @editor (get-data id))))

        :reagent-render
        (fn []
          [:div {:class (if @active-a "editor-base-active"
                            "editor-base-passive")}])}))))