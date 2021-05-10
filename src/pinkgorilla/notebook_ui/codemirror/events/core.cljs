(ns pinkgorilla.notebook-ui.codemirror.events.core
  (:require
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [pinkgorilla.notebook-ui.codemirror.codemirror :refer [configure-cm-globally!]]))

(reg-event-fx
 :codemirror/init
 (fn [_ _]
   (configure-cm-globally!)))

(reg-event-db
 :codemirror/set-active
 (fn [db [_ segment-id cm]]
   (assoc-in db [:codemirror] {:id segment-id
                               :cm cm})))

