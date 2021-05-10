
(ns pinkgorilla.notebook-ui.datafy.events-punk
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch reg-sub]]))

(reg-event-db
 :punk/init
 (fn [db [_]]
   (info ":punk/init - datafy")
   (assoc db :punk {:entries []
                    :history []
                    :current nil
                    :current/loading false
                    :current.view/selected nil
                    :next nil
                    :next.view/key nil
                    :next.view/selected nil})))

(reg-sub
 :punk
 (fn [db _]
   (get-in db [:punk])))

(reg-event-fx
 :punk.ui.browser/view-entry
 (fn [{:keys [db]} [_ x]]
   (info "punk view-entry: " x)
   {:db (update db
                :punk
                assoc
                :current x
                :current/loading false
                :current.view/selected nil
                :next nil
                :history [])}))

(reg-event-fx
 :punk.ui.browser/history-back
 (fn [{:keys [db]} _]
   {:db (-> db
            (update-in [:punk :history] pop)
            (update :punk
                    assoc
                    :current (-> db :punk :history peek)
                    :next nil
                    :next.view/selected nil
                    :current.view/selected nil))}))

(reg-event-fx
 :punk.ui.browser/history-nth
 (fn [{:keys [db]} [_ idx]]
   (let [current (nth (get-in db [:punk :history]) idx)]
     {:db (-> db
              (update :punk
                      assoc
                      :history (vec (take idx (get-in db [:punk :history])))
                      :current current
                      :next nil
                      :next/key (:nav-key current)
                      :next.view/selected nil
                      :current.view/selected nil))})))

(reg-event-fx
 :punk.ui.browser/preview
 (fn [{:keys [db]} [_ idx k v]]
   (let [current (get-in db [:punk :current])
         next-val (get current k v)
         _ (info "preview idx:" idx "key:" k "val: " next-val)
         next-meta (meta next-val)]
     {:db (update
           db
           :punk
           assoc
           :next.view/key k
           :next.view/selected nil
           :next {:key k
                  :value next-val
                  :meta next-meta})})))

(reg-event-fx
 :punk.ui.browser/nav-to-next
 (fn [{:keys [db]} [_]]
   (let [{:keys [next current]} (:punk db)
         {:keys [idx]} current ; idx of current
         {:keys [key value]} next ; key/value of next
         ]
     (info "nav-to-next idx:" idx "key: " key "value: " value)
     (dispatch [:datafy/nav idx key value])
     {:db (-> db
              (update
               :punk
               assoc
               :current/loading true
               :next.view/selected nil
               :next nil))})))

(reg-event-fx
 :punk.ui.browser/select-next-view
 []
 (fn [{:keys [db]} [_ id]]
   {:db (assoc-in db [:punk :next.view/selected] id)}))

(reg-event-fx
 :punk.ui.browser/select-current-view
 []
 (fn [{:keys [db]} [_ id]]
   {:db (assoc-in db [:punk :current.view/selected] id)}))

;;
;; Punk events
;;

(reg-event-fx
 :punk/tap-response
 (fn [cofx [_ idx x]]
   {:db (update-in
         (:db cofx)
         [:punk :entries]
         conj
         (assoc x :idx idx))}))

(reg-event-fx
 :punk/nav-response
 (fn [{:keys [db]} [_ idx x]]
   {:db
    (-> db
        (update-in [:punk :history]
                   conj
                   (assoc (get-in db [:punk :current])
                          :nav-key (get-in db [:punk :next.view/key])))
        (update :punk
                assoc
                :current/loading false
                :current x
                :current.view/selected nil
                :next.view/key nil))}))







