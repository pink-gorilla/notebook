(ns pinkgorilla.storage.storage-handler
  (:require
   [clojure.tools.logging :refer (info)]
   [ring.util.response :as res]

   ; make sure all the prototypes are here.
   [pinkgorilla.storage.github]
   [pinkgorilla.storage.storage :refer [query-params-to-storage storage-save storage-load]]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]))


;; the client can post a request to have the worksheet saved, handled by the following


(defn save-notebook
  [req]
  (let [params (:params req)
        _ (info "saving notebook with params: " (dissoc params :notebook)) ; do not show full notebook in log.
        stype (keyword (:storagetype params))
        notebook (:notebook params)
        tokens (:tokens params)
        _ (info "saving notebook with tokens: " tokens)
        storage-params (dissoc params :notebook :storagetype :tokens) ; notebook-content is too big for logging.
        ;_ (info "Saving type: " stype " params: " storage-params)
        storage (query-params-to-storage stype storage-params)
        ;_ (info "Notebook: " notebook)
        ]
    (if (nil? storage)
      (throw (Exception. (str "Cannot save to storage - storage is nil! " stype)))
      (do
        (info "Saving: " storage)
        (res/response (storage-save storage notebook tokens))))))

(defn load-notebook
  [req]
  (let [params (:params req)
        stype (keyword (:storagetype params))
        tokens (:tokens params)
        storage-params (dissoc params :storagetype :tokens) ; notebook-content is too big for logging.
        ;_ (info "Saving type: " stype " params: " storage-params)
        storage (query-params-to-storage stype storage-params)]
    (if (nil? storage)
      (throw (Exception. (str "cannot load from storage - storage is nil! " stype)))
      (do
        (info "Loading from storage: " storage)
        (if-let [content (storage-load storage tokens)]
          (res/response {:content content})
          {:status 500 :body "No content"})))))


