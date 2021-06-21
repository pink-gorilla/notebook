
(ns pinkgorilla.notebook-ui.settings.events
  "events related to the settings dialog"
  (:require
   [taoensso.timbre :refer-macros [info]]
   [cljs.reader :as rd]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [pinkgorilla.notebook-ui.settings.component :refer [settings-dialog]]))

; dialog visibility

(reg-event-db
 :settings/show
 (fn [db [_]]
   (info "showing settings dialog")
   (dispatch [:modal/open [settings-dialog] :medium])
   db))

;; secrets

(reg-event-fx
 :set-clj-secrets
 (fn [{:keys [db]}]
   (let [secrets (get-in db [:settings :secrets])
         secrets (if (nil? secrets) {} secrets)]
     (info "setting clj repl secrets..")
     ;(clj-eval-ignore-result "pinkgorilla.notebook.secret/set-secrets!" secrets)
     {})))

(reg-event-db
 :secret/add
 (fn [db [_ s]]
   (info "adding secret:" (:name s))
   (dispatch [:set-clj-secrets]) ; push new secrets to clj
   (assoc-in db [:settings :secrets (keyword (:name s))] (:secret s))))

(reg-event-db
 :secret/remove
 (fn [db [_ n]]
   (info "removing secret:" n)
   (dispatch [:set-clj-secrets]) ; push new secrets to clj
   (update-in db [:settings :secrets] dissoc n)))

(reg-event-db
 :secret/import
 (fn [db [_ s]]
   (info "importing secrets:" s)
   (dispatch [:set-clj-secrets]) ; push new secrets to clj
   (assoc-in db [:settings :secrets] (cljs.reader/read-string s))))
