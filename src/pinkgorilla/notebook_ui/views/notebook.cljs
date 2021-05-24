(ns pinkgorilla.notebook-ui.views.notebook
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :as rf]
   [ui.notebook.core :refer [notebook-view]]))

(defn notebook-storage-viewer [storage notebook]
  (info "viewing notebook from storage " storage)
  (rf/dispatch [:notebook/activate! storage])
  (fn [storage notebook]
    [notebook-view]))

