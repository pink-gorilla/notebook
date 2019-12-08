(ns pinkgorilla.events.explore
  "load list of explored notebooks"
  (:require
   [ajax.core :as ajax :refer [GET POST]]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]

   ; dependencies needed to be in cljs bundle:
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]
   [pinkgorilla.storage.storage :refer [create-storage]]

   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))

(reg-event-fx
 :explore-load
 (fn [{:keys [db]} _]
   {:db         (merge db {:message "Exploring notebooks ..."})
    :http-xhrio {:method          :get
                 :uri             "https://raw.githubusercontent.com/pink-gorilla/gorilla-explore/master/resources/list.json"
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


(defn preprocess-item [idx item]
  (-> item
      (assoc :type (keyword (:type item)) :index idx)
      (remove-repo-id)
      (add-storage)))


(defn preprocess-list [response]
  (let [list (:data response)]
    (vec (map-indexed preprocess-item list))))


(reg-event-db
 :explore-response
 [standard-interceptors]
 (fn [db [_ response]]
   (-> (assoc-in db [:data :projects] (preprocess-list response))
       (assoc :message nil))))


(reg-event-db
 :goto-main
 [standard-interceptors]
 (fn [db [_ response]]
   (-> (assoc-in db [:main] :notebook
       ))))
