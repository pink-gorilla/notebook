(ns pinkgorilla.notebook-ui.datafy.events-dialog
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [reg-event-db dispatch reg-sub]]
   [pinkgorilla.notebook-ui.datafy.dialog :refer [datafy-dialog]]))

(reg-event-db
 :punk/set-entries
 (fn [db [_ entries]]
   (assoc-in db [:punk :entries] entries)))

(reg-event-db
 :datafy/show
 (fn [db [_ datafy]]
   (let [entries [datafy]]
     (info "showing datafy dialog: " entries) ; [{:idx 7, :value nil, :meta nil}]
     (dispatch [:punk/init])
     (dispatch [:punk.ui.browser/view-entry datafy])
     (dispatch [:modal/open
                [datafy-dialog]
                :large])
     (-> db
         (assoc-in [:punk :entries] entries)))))

(reg-sub
 :datafy/data
 (fn [db _]
   (get-in db [:datafy])))