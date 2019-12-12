(ns pinkgorilla.events.storage
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [ajax.core :as ajax]
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.notebook.core :refer [load-notebook-hydrated save-notebook-hydrated]]
   [pinkgorilla.storage.storage :refer [storagetype query-params-to-storage gorilla-path]]
   [pinkgorilla.events.helper :refer [standard-interceptors]]
   [pinkgorilla.notifications :as events :refer [add-notification notification]]))


;; Load File (from URL Parameters) - in view or edit mode

;; http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj
;; http://localhost:3449/worksheet.html#/view?source=gist&id=2c210794185e9d8c0c80564db581b136&filename=new-render.clj


#_(reg-event-fx
   :view-file
   (fn [{:keys [db]} [_ params]]
     (let [storage-type (keyword (:source params))
           storage (query-params-to-storage storage-type params)
           url (load-url storage (:base-path db))
           _ (info "loading from storage:" storage-type " url: " url)]
       {:db         db
        :http-xhrio {:method          :get
                     :uri             url
                     :timeout         5000
                     :response-format (ajax/json-response-format)
                     :on-success      [:process-load-file-response storage]
                     :on-failure      [:process-error-response]}})))

(reg-event-fx
 :edit-file
 (fn [{:keys [db]} [_ params]]
   (let [_ (info "edit-file params:" params)
         stype (keyword (:source params))
         storage (query-params-to-storage stype params)
         ;url (load-url storage (:base-path db))
         ;_ (info "loading from storage:" stype " url: " url)
         tokens (:settings db)
         url  (str (:base-path db) "load")
         params (assoc storage
                       :storagetype stype
                       :tokens tokens)]
     ;(dispatch [:ga/event :notebook :load])
     {:db         (assoc-in db [:main] :notebook) ; notebook view on loading
      ;; :ga/event [:notebook-load]
      :http-xhrio {:method          :get
                   :uri             url
                   :params          params
                   :timeout         15000
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:process-load-file-response storage]
                   :on-failure     [:process-load-file-response-error storage]}})))

(reg-event-db
 :process-load-file-response-error
 [standard-interceptors]
 (fn
   [db [_ _ response-body]] ; _ storage
   (dispatch [:notification-add (notification :warning "Load Notebook")])
   (let [_ (info "Load Response Error:\n" response-body)
         content (:content response-body)
         _ (info "Content Only:\n" content)]
     (assoc db
            :worksheet {:meta {}}
            :storage-load-error content
            :storage nil))))

(reg-event-db
 :process-load-file-response
 [standard-interceptors]
 (fn
   [db [_ storage response-body]]
   (let [;_ (info "Load Response:\n" response-body)
         content (:content response-body)
         ;content (decode-content storage content)
         ; _ (info "Content Only:\n" content)
         notebook (if (nil? content)
                    nil
                    (load-notebook-hydrated content))
         ;_ (info "notebook: " notebook)
         ]
     (assoc db
            :worksheet notebook
            :storage storage))))

;; SAVE File

(reg-event-db
 :save-notebook
 [standard-interceptors]
 (fn [db _]
   (if-let [storage (get-in db [:storage])]
     (dispatch [:save-storage storage]) ; just save to storage if we have a storage
     (dispatch [:app:saveas])) ;otherwise open save dialog
   db))

;; save using the storage protocol
(reg-event-fx
 :save-storage
 (fn [{:keys [db]} [_ storage]]
   (let [stype (storagetype storage)
         _ (info "notebook saving to storage " stype)
         notebook (save-notebook-hydrated (:worksheet db))
         tokens (:settings db)
         url  (str (:base-path db) "save")
         params (assoc storage
                       :storagetype stype
                       :notebook notebook
                       :tokens tokens)]
     {:db         (assoc-in db [:dialog :save] false)
      :http-xhrio {:method          :post
                   :uri             url
                   :params          params
                   :timeout         5000                     ;; optional see API docs

                   ;; awb99: transit request does not work - possibly missing dependency?
                   ;; awb99: url-request format does not work because server has problem decoding token maps
                   :format       (ajax/json-request-format {:keywords? true}) ; (ajax/transit-request-format) ;  (ajax/url-request-format) ; request encoding POST body url-encoded
                   :response-format (ajax/json-response-format {:keywords? true}) ;(ajax/transit-response-format) ;; response encoding TRANSIT
                   :on-success      [:after-save-success storage]
                   :on-failure      [:notification-add (notification :warning "save-notebook ERROR!!")]}})))

(defn hack-gist [storage result db]
  (if (and (= (:id storage) nil)
           (= :gist (storagetype storage)))
    (do
      (routes/nav! (gorilla-path (assoc storage :id (:id result))))
      (assoc-in db [:storage :id] (:id result)))
    db))

;; display success message when saving was successful
(reg-event-db
 :after-save-success
 [standard-interceptors]
 (fn [db [_ storage result]]
   (info "Storage is:" storage ", result is: " result)
   (add-notification (notification :info "Notebook saved."))
   (hack-gist storage result db))   ;(routes/nav! (str "/edit?source=local&filename=" filename))
 )







