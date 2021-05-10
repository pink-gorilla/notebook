(ns pinkgorilla.notebook-ui.codemirror.cm-events.mouse
  (:require
   [taoensso.timbre :refer-macros [debug debugf info infof error]]
   [re-frame.core :refer [dispatch]]))

(defn on-mousedown [{:keys [cm-opts cm id]} sender evt]
  (info "on-mousedown segment-id: " id)
  (dispatch [:notebook/set-cm-md-edit true])
  (dispatch [:notebook/move :to id]))