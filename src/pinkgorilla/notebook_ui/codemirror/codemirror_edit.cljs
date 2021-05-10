(ns pinkgorilla.notebook-ui.codemirror.codemirror-edit
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.codemirror.codemirror :refer [codemirror-reagent]]))

(defn codemirror-edit [id cm-opt]
  (let [;settings (subscribe [:settings])
        ]
    (fn [id {:keys [segment-active?] :as cm-opt}]
      (let [theme "paraiso-dark" ;"(or (:codemirror-theme @settings) "paraiso-dark")
            ;readOnly (not (and segment-active? cm-md-edit?))
            #__ #_(info "id:" id
                        "cm-md-edit?:" cm-md-edit?
                        "readOnly:" readOnly)
            cm-opt (merge cm-opt {;:readOnly readOnly
                                  :theme theme})]
        ;(info "render codemirror-edit " id code)
        [:div.my-codemirror
         [codemirror-reagent id cm-opt]]))))



