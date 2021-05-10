(ns pinkgorilla.notebook-ui.kernel.picasso
  (:require
   [taoensso.timbre :refer-macros [debug]]
   [picasso.protocols :refer [paint make]]
   [pinkie.text :refer [text]]
   [pinkie.html :refer [html]]
   [pinkie.pinkie-render :refer [pinkie-render]]))

; picasso in nrepl middleware converts value to picasso-spec
; picasso-spec gets rendered to reagent with paint.
; picasso defines :reagent and :hiccup
; todo: add hiccup style-as-string to style-as-map converter.


(defmethod paint :text [{:keys [#_type content]}]
  [text content])

#_(defmethod paint :html [{:keys [#_type content]}]
    [html content])

(defmethod paint :html [{:keys [#_type content]}]
  (if (string? content)
    [html content]
    (paint (make :hiccup content))))

(defmethod paint "html" [{:keys [#_type content]}]
  (paint (make :html content)))

(defmethod paint :pinkie [{:keys [#_type content]}]
  [pinkie-render {:map-keywords true} content])

(defmethod paint :reagent [{:keys [#_type content]}]
  (let [{:keys [hiccup map-keywords]} content]
    (if map-keywords
      (do (debug "rendering reagent with pinkie..")
          (paint (make :pinkie hiccup)))
      (do (debug "rendering reagent direct ..")
          hiccup))))

(defmethod paint :reagent-cljs [{:keys [#_type content]}]
  (let [{:keys [hiccup]} content]
    (if hiccup
      hiccup
      [:h1
       {:class "bg-red-300"}
       "Reagent-cljs (please evaluate cell. Reagent-cljs can not get persisted.)"])))



