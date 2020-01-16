(ns pinkgorilla.events.storage-file
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
   [ajax.core :as ajax]
   ;; [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.routes] ;; Needed for defroute?
   [pinkgorilla.events.helper :refer [standard-interceptors]]))


;; load files from local-file-storage
;; present dialog of available local files


(reg-event-fx
 :browse-files-local
 (fn [{:keys [db]} _]
   {:db         db
    :http-xhrio {:method          :get
                 :uri             (str (:base-path db) "gorilla-files")
                 :timeout         5000                     ;; optional see API docs
                 :response-format (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:process-files-response]
                 :on-failure      [:process-error-response "browse-file-local"]}}))

(reg-event-db
 :process-files-response
 [standard-interceptors]
 (fn
   [db [_ response]]
   (let [file-items (->> (:files response)
                         (map (fn [x] {:text    x
                                       :desc    (str "<div class=\"command\">" x "</div>")
                                       ;; For now, we have to take/return db due to clojuredocs sync window.open
                                       :handler [:nav (str "/edit?source=file&filename=" x) db]})))
         palette (:palette db)]
     (assoc-in db [:palette] (merge palette {:all-items     file-items
                                             :visible-items file-items
                                             :show          true
                                             :label         "Choose a file to load:"})))))






