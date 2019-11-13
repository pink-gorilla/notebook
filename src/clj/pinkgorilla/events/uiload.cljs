(ns pinkgorilla.events.uiload
  (:require
   [ajax.core :as ajax :refer [GET POST]]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [pinkgorilla.db :as db :refer [initial-db]]
   [pinkgorilla.notebook.core :refer [save-notebook-hydrated]]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))


(reg-event-fx                                               ;; note the trailing -fx
 :app:load
 (fn [{:keys [db]} _]
   {:db         db
    :http-xhrio {:method          :get
                 :uri             (str (:base-path db) "gorilla-files")
                 :timeout         5000                     ;; optional see API docs
                 :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:process-files-response]
                 :on-failure      [:process-error-response]}}))


(reg-event-db
 :app:save
 [standard-interceptors]
 (fn [db _]
   (if-let [filename (get-in db [:save :filename])]
     (dispatch [:save-file filename])
     (dispatch [:app:saveas]))
   db))

(reg-event-db
 :app:saveas
 [standard-interceptors]
 (fn [db _]
   (assoc-in db [:save :show] true)))


(reg-event-db
 :after-save
 [standard-interceptors]
 (fn [db [_ filename]]
   (dispatch [:display-message (str filename " saved") 2000])
   (routes/nav! (str "/edit?worksheet-filename=" filename))
   (assoc-in db [:save :saved] true)))

(reg-event-fx                                               ;; note the trailing -fx
 :save-file
 (fn [{:keys [db]} [_ filename]]
   {:db         (assoc-in db [:save :show] false)
    :http-xhrio {:method          :post
                 :body            (str "worksheet-filename=" (js/encodeURIComponent filename)
                                       "&worksheet-data=" (-> (save-notebook-hydrated (:worksheet db))
                                                              js/encodeURIComponent))
                 :uri             (str (:base-path db) "save")
                 :timeout         5000                     ;; optional see API docs
                 :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:after-save filename]
                 :on-failure      [:process-error-response]}}))

(reg-event-db
 :save-as-keydown
 (fn [db [_ keycode]]
   (case keycode
     27 (do                                                ;; esc
          (dispatch [:save-as-cancel])
          db)
     13 (do                                                ;; Enter
          (dispatch [:save-file (get-in db [:save :filename])])
          db)
     db)))

(reg-event-db
 :save-as-change
 [standard-interceptors]
 (fn [db [_ value]]
   (assoc-in db [:save :filename] value)))


(reg-event-db
 :save-as-cancel
 [standard-interceptors]
 (fn [db _]
   (assoc-in db [:save] {:show     false
                         :filename nil})))


