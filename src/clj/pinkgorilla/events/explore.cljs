(ns pinkgorilla.events.explore
  "load list of explored notebooks"
  (:require
   [taoensso.timbre :as timbre
    :refer-macros (info)]
   [ajax.core :as ajax]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx dispatch]]

   [pinkgorilla.events.helper :refer [standard-interceptors]]
   [pinkgorilla.notifications :as events :refer [add-notification notification]]

   ; dependencies needed to be in cljs bundle:
   [pinkgorilla.storage.storage :as storage :refer [create-storage]]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]))



(reg-event-fx
 :explore-load-repository
 (fn [{:keys [db]} [_ repository]]
   (add-notification (notification :info (str "Exploring " (:name repository))))
   {:db        db ;  (merge db {:message "Exploring notebooks ..."})
    :http-xhrio {:method          :get
                 :uri             (:url repository)
                 :timeout         10000                     ;; optional
                 :response-format (ajax/json-response-format {:keywords? true}) ; (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:explore-response]
                 :on-failure      [:process-error-response "load-explore-data"]}}))


(defn remove-repo-id [item]
  (if (= (:type item) :repo)
    (dissoc item :id)
    item))

(defn add-storage [item]
  (assoc item :storage (create-storage item)))


(defn preprocess-item [start-index idx item]
  (-> item
      (assoc :type (keyword (:type item)) :index (+ start-index idx))
      (remove-repo-id)
      (add-storage)))


(defn preprocess-list [start-index response]
  (let [list (:data response)]
    (vec (map-indexed (partial preprocess-item start-index) list))))


(reg-event-db
 :explore-response
 [standard-interceptors]
 (fn [db [_ response]]
   (let [existing-data (get-in db [:data :projects])
         start-index (count existing-data)
         new-data (preprocess-list start-index response)]
     (-> (assoc-in db [:data :projects] (concat existing-data new-data))
       ;(assoc :message nil)
         ))))


(reg-event-db
 :goto-main
 [standard-interceptors]
 (fn [db _]
   (assoc-in db [:main] :notebook)))


(info "registering :explore-load ..")

(reg-event-fx
 :explore-load
 (fn [{:keys [db]}]
   (dispatch [:explore-load-repository {:name "public" :url "https://raw.githubusercontent.com/pink-gorilla/gorilla-explore/master/resources/list.json"}])
   (dispatch [:explore-load-repository {:name "files" :url "/explore"}])
   {:db db}))
