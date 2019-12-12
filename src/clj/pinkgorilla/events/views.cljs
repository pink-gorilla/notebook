(ns pinkgorilla.events.views
  (:require
   [re-frame.core :refer [reg-event-fx reg-event-db after dispatch] :include-macros true]
   [cljs.core.async :as async]
   [pinkgorilla.events.common :as common-events :refer [reg-set-attr]]
   ;[day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

;; stolen from:
;; https://github.com/baskeboler/cljs-karaoke-client/blob/master/src/main/cljs_karaoke/events/views.cljs

(def view-states
  {:home     {:go-to-playback :playback
              :go-to-home     :home
              :load-song      :playback
              :play           :home
              :go-to-playlist :playlist}
   :playback {:play           :playback
              :stop           :playback
              :load-song      :playback
              :go-to-home     :home
              :go-to-playback :playback
              :go-to-playlist :playlist}
   :playlist {:go-to-playback :playback
              :go-to-home     :home
              :play           :playlist
              :stop           :playlist
              :load-song      :playlist}
   :admin {}})

(def transition  (partial  view-states))

(def initial-views-state
  {:home {}
   :playback {:options-enabled? false}})

(reg-event-fx
 ::init-views-state
 (fn ; fn-traced
   [{:keys [db]} _]
   {:db (-> db
            (assoc :views initial-views-state))
    :dispatch [::views-state-ready]}))

(reg-event-db
 ::views-state-ready
 (fn ; fn-traced
   [db _]
   (. js/console (log "views state ready"))
   (-> db
       (assoc :views-state-ready? true))))

(reg-event-db
 ::set-view-property
 (fn ; fn-traced
   [db [_ view-name property-name property-value]]
   (-> db
       (assoc-in [:views view-name property-name] property-value))))

(reg-event-db
 ::set-seek-buttons-visible
 (fn ; fn-traced
   [db [_ visible]]
   (-> db (assoc :seek-buttons-visible? visible))))

(reg-event-fx
 ::show-seek-buttons
 (after
  (fn [_ _] ;
    (async/go
      (async/<! (async/timeout 5000))
      (dispatch [::set-seek-buttons-visible false]))))
 (fn ; fn-traced
   [{:keys [db]} _]
   {:db db
    :dispatch [::set-seek-buttons-visible true]}))

(reg-event-db
 ::set-display-home-button
 (fn ; fn-traced
   [db [_ display-button?]]
   (-> db (assoc :display-home-button? display-button?))))

(reg-set-attr ::set-current-view :current-view)

(reg-event-fx
 ::view-action-transition
 (fn ; fn-traced
   [{:keys [db]} [_ action]]
   (let [current (:current-view db)
         next-view (get (transition current) action :error)]
     {:db db
      :dispatch [::set-current-view next-view]})))
