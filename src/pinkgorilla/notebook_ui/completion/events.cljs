(ns pinkgorilla.notebook-ui.completion.events
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [reg-event-fx reg-event-db dispatch]]
   [pinkgorilla.nrepl.client.core :refer [op-resolve-symbol op-docstring op-completions]]))

(reg-event-db
 :completion/init
 (fn [db [_]]
   (let [db (or db {})]
     (assoc db
            :completion
            {:word nil
             :candidates []
             :active nil
             :show-all false
             :docstring ""
             :resolve nil}))))

; map cider operations:

(reg-event-db
 :completion/save-result
 (fn [db [_ result]]
   (let [c (:completions result)]
     (info "rcvd completion candidates: " result)
     (-> db
         (assoc-in [:completion :candidates] c)
         (assoc-in [:completion :active] (first c))))))

(reg-event-fx
 :nrepl/completion
 (fn [cofx [_ q namespace context]]
   (dispatch [:nrepl/op-dispatch (op-completions q namespace context) [:completion/save-result]])))

(reg-event-fx
 :nrepl/docstring
 (fn [cofx [_ symbol namespace]] ; "pprint-table" "clojure.pprint"
   (dispatch [:nrepl/op-db (op-docstring symbol namespace) [:completion]])))

(reg-event-fx
 :nrepl/resolve-symbol
 (fn [cofx [_ symbol namespace]] ; "doseq" "clojure.core"
   (dispatch [:nrepl/op-db (op-resolve-symbol symbol namespace) [:completion]])))



