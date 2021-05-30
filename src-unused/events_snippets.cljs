(ns pinkgorilla.notebook-ui.events.events-snippets
  (:require
   [taoensso.timbre :as timbre :refer [debug warn error] :refer-macros [info errorf]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db dispatch]]
   [pinkgorilla.storage.unsaved :refer [StorageUnsaved]]
   [pinkgorilla.notebook.template :refer [snippets->notebook]]
   [pinkgorilla.notebook.hipster :refer [make-hip-nsname]]))

(reg-event-db
 :document/load-snippets
 (fn [db [_ snippets]]
   (let [id (make-hip-nsname)
         _ (info "loading snippets to document:" id)
         storage (StorageUnsaved. id)
         document-dehydrated (snippets->notebook snippets)
         document  document-dehydrated
         db-new (assoc-in db [:document :documents storage] document)]
     (dispatch [:notebook/activate! storage])
     db-new)))

