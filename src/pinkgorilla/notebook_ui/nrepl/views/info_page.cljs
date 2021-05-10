(ns pinkgorilla.notebook-ui.nrepl.views.info-page
  (:require
   [taoensso.timbre :refer-macros [infof]]
   [re-frame.core :refer [subscribe]]
   [pinkgorilla.notebook-ui.nrepl.views.connect :refer [connect-ui]]
   [pinkgorilla.notebook-ui.nrepl.views.panel :refer [info-panel]]))

(defn nrepl-info []
  (let [ninfo (subscribe [:nrepl/info])
        nconn (subscribe [:nrepl/conn])]
    (fn []
      (let [{:keys [describe sessions middleware sniffer-status]} @ninfo
            {:keys [session-id]} @nconn
            ;_ (infof "nrepl session %s info: %s" session-id describe)
            ]
        [:div
         [connect-ui]
         [info-panel session-id describe sessions middleware sniffer-status]]))))

