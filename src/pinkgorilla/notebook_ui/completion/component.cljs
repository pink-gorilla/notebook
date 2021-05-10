(ns pinkgorilla.notebook-ui.completion.component
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]
   [reagent.ratom :refer [reaction]])
  (:require
   [taoensso.timbre :refer-macros [debug info]]
   [cljs.core.async :as async :refer [<! >! chan timeout close!]]
   [reagent.core :as r]
   [re-frame.core :refer [reg-event-db reg-sub  subscribe dispatch]]
   [webly.user.notifications.core :refer [add-notification]]
   [pinkgorilla.notebook-ui.completion.view :refer [completion-list]]
   [pinkgorilla.notebook-ui.completion.docstring :refer [docs-view]]))

; active completion component:

(reg-event-db
 :completion/hint
 (fn [db [_ word namespace context]]
   (let [current-word (get-in db [:completion :word])]
     (if (not (= current-word word))
       (do
         (info "getting complete for: " word)
         (add-notification :danger (str "completing: " word))
         (dispatch [:nrepl/completion word namespace context])
         (assoc-in db [:completion :word] word))
       (do
         (info "completion word unchanged: " word)
         (dispatch [:completion/next])
         db)))))

(reg-event-db
 :completion/select
 (fn [db [_ active]]
   (dispatch [:nrepl/docstring (:candidate active) (:ns active)])
   (assoc-in db [:completion :active] active)))

(defn indices [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn current-index [active candidates]
  (first (indices #(= active %) candidates)))

(reg-event-db
 :completion/next
 (fn [db [_]]
   (info ":completion/next")
   (when-let [candidates (get-in db [:completion :candidates])]
     (let [active (get-in db [:completion :active])
           idx (current-index active candidates)
           n-idx (min (- (count candidates) 1) (+ idx 1))
           n-active (get candidates n-idx)]
       (info "next completion: " n-active)
       (dispatch [:completion/select n-active])))
   db))

(reg-event-db
 :completion/prior
 (fn [db [_]]
   (info ":completion/prior")
   (when-let [candidates (get-in db [:completion :candidates])]
     (let [active (get-in db [:completion :active])
           idx (current-index active candidates)
           p-idx (max 0 (- idx 1))
           p-active (get candidates p-idx)]
       (info "prior completion: " p-active)
       (dispatch [:completion/select p-active])))
   db))

(reg-sub
 :completion
 (fn [db _]
   (get-in db [:completion])))

(reg-event-db
 :completion/show-all
 (fn [db [_ show-all]]
   (info "completion show-all: " show-all)
   (assoc-in db [:completion :show-all] show-all)))

(reg-event-db
 :completion/show-all-toggle
 (fn [db [_]]
   (let [show-all (get-in db [:completion :show-all])]
     (info "completion show-all toggle: " show-all)
     (assoc-in db [:completion :show-all] (not show-all)))))

#_(reg-event-db
   :completion/hide
   (fn [db [_]]
     (info "hiding completions")
     db))

#_(reg-event-db
   :completion/clear
   (fn [db [_]]
     (info "clearing completions")
     db))

(defn completion-component []
  (let [ncomp (subscribe [:completion])]
    (fn []
      (let [i @ncomp
            {:keys [show-all active docstring candidates]} i
            _ (debug "nrepl-completion candidates: "  candidates " docstring: " docstring)]
        [:div
         [:h1 "completions show-all: " (str show-all)]
         [:p show-all]
         ;(when i
         [completion-list {:pos 1
                           :list candidates ;  (map :candidate cc)
                           :active active
                           :show-all show-all
                           :set-active (fn [a]
                                         (info "set active: " a)
                                         (dispatch [:completion/select a]))}]
         [docs-view docstring]
         ;[completion-view cc ds]
         ]))))

