(ns pinkgorilla.notebook-ui.views.notebook
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.notebook-ui.hydration :refer [segments-ordered]]
   [pinkgorilla.notebook-ui.views.layout :refer [notebook-layout]]))

(defn notebook-component []
  (let [settings (subscribe [:settings])
        notebook (subscribe [:notebook])
        segment-active (subscribe [:notebook/segment-active])]
    (fn []
      (let [segments (segments-ordered @notebook)]
        [:div.w-full.h-full.min-h-full.bg-gray-100 ; .overflow-scroll
         [notebook-layout @settings @segment-active segments]]))))

(defn notebook-storage-viewer [storage notebook]
  (info "viewing notebook from storage " storage)
  (dispatch [:notebook/activate! storage])
  (fn [storage notebook]
    [notebook-component]))

