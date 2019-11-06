(ns pinkgorilla.events.persistence
  (:require
   [goog.crypt.base64 :as b64]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [ajax.core :as ajax :refer [GET POST]]
   [taoensso.timbre :refer-macros (info)]
   [pinkgorilla.notebook.core :refer [load-notebook-hydrated]]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]))


(defmulti loaded-worksheet identity)

(defmethod loaded-worksheet :default
  [_ _ response]
  response)

(defmethod loaded-worksheet :gist
  [_ filename response]
  (let [files (get response "files")
        name (if (= 1 (count files))
               (first (keys files))
               filename)
        content (-> (get files name)
                    (get "content"))]
    content))

(defmethod loaded-worksheet :github
  [_ _ response]
  (b64/decodeString (get response "content")))

(defmethod loaded-worksheet :local
  [_ _ content]
  (get content "worksheet-data"))

(reg-event-db
 :process-load-file-response
 [standard-interceptors]
 (fn
   [db [_ source filename content]]
   (let [content (loaded-worksheet source filename content)
         _ (info "Content loaded: " content)
         worksheet (load-notebook-hydrated content)
         save (assoc (:save db) :filename filename)]
     (assoc db
            :worksheet worksheet
            :save save))))


(reg-event-db
 :process-files-response
 [standard-interceptors]
 (fn
   [db [_ response]]
   (let [file-items (->> (:files response)
                         (map (fn [x] {:text    x
                                       :desc    (str "<div class=\"command\">" x "</div>")
                                       ;; For now, we have to take/return db due to clojuredocs sync window.open
                                       :handler (fn [db]
                                                  (routes/nav! (str "/edit?worksheet-filename=" x))
                                                  db)})))
         palette (:palette db)]
     (assoc-in db [:palette] (merge palette {:all-items     file-items
                                             :visible-items file-items
                                             :show          true
                                             :label         "Choose a file to load:"})))))


(reg-event-fx                                               ;; note the trailing -fx
 :edit-file
 (fn [{:keys [db]} [_ filename]]
   {:db         (update-in db [:save] dissoc :saved)
    :http-xhrio {:method          :get
                 :uri             (str (:base-path db)
                                       "load?worksheet-filename="
                                       (js/encodeURIComponent filename))
                 :timeout         5000
                 :response-format (ajax/json-response-format)
                 :on-success      [:process-load-file-response :local filename]
                 :on-failure      [:process-error-response]}}))


(reg-event-fx                                               ;; note the trailing -fx
 :view-file
 (fn [{:keys [db]} [_ params]]
   (let [source (:source params)
         user (:user params)
         repo (:repo params)
         path (:path params)
         id (:id params)
         filename (:filename params)
         revision (or (:revision params) "HEAD")
         response-type (case source
                         "github"
                         :github
                         "gist"
                         :gist
                         :default)
         url
         (case source
           "github"
           (str "https://api.github.com/repos/" user "/" repo "/contents/" path)
           "gist"
           (str "https://api.github.com/gists/" id)
           "bitbucket"
           (str "https://bitbucket.org/api/1.0/repositories/" user "/" repo "/raw/" revision "/" path)
           "test"
           "/test.clj"
           nil)]
     {:db         db
      :http-xhrio {:method          :get
                   :uri             url
                   :timeout         5000
                   :response-format (ajax/json-response-format)
                   :on-success      [:process-load-file-response response-type filename]
                   :on-failure      [:process-error-response]}})))
