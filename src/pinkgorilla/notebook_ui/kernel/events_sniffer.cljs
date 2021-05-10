(ns pinkgorilla.notebook-ui.kernel.events-sniffer
  (:require
   [taoensso.timbre :as timbre :refer-macros [info errorf debug warn error]]
   [cljs.reader]
   [cljs.tools.reader]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [webly.user.notifications.core :refer [add-notification]]
   [pinkgorilla.nrepl.client.op.eval :refer [process-fragment initial-value]]
   [pinkgorilla.storage.unsaved :refer [StorageUnsaved]]
   [pinkgorilla.notebook.template :refer [snippets->notebook]]
   [pinkgorilla.notebook-ui.hydration  :refer [hydrate create-code-segment insert-segment-bottom]]))

(def id-doc-sniffer "sniffer-notebook")

(def sniffer-snippets
  ["; sniffed evals:"])

(defn create-notebook []
  (let [document-dehydrated (snippets->notebook sniffer-snippets)
        document (hydrate document-dehydrated)]
    document))

(reg-event-db
 :sniffer/add-document
 (fn [db [_ notebook]]
   (let [storage (StorageUnsaved. id-doc-sniffer)
         db-new (assoc-in db [:document :documents storage] notebook)]
     ;(dispatch [:notebook/activate! storage])
     db-new)))

#_(reg-event-fx
   :sniffer/init
   (fn [{:keys [db] :as cofx} [_]]
     (dispatch [:sniffer/create-document])))

(defn- add-code-segment
  "returns new notebook with code from msg appended"
  [notebook msg]
  (let [{:keys [code id]} msg
        id-kw (keyword id)
        _ (debug "sniffed code id: " id-kw " code:" code)
        segment-new (create-code-segment code)
        segment-new (assoc segment-new :id id-kw)
        segment-new (merge segment-new initial-value)]
    (insert-segment-bottom notebook segment-new)))

(defn- add-result
  "returns new notebook with eval-result from msg appended"
  [notebook msg]
  (debug "sniffed result: " msg)
  (let [{:keys [id]} msg
        seg-id (keyword id)
        segment (get-in notebook [:segments seg-id])
        result-new (process-fragment segment msg)
        segment-new (merge segment result-new)]
    (assoc-in notebook [:segments seg-id] segment-new)))

(defn get-notebook
  "returns [path notebook]
   if notebook has current document, return it
   otherwise return sniffer notebook
   create sniffer notebook if not existing
   "
  [db]
  (let [storage-current (get-in db [:notebook])
        path-current [:document :documents storage-current]
        notebook-current (get-in db path-current)
        storage-sniffer (StorageUnsaved. id-doc-sniffer)
        path-sniffer [:document :documents storage-sniffer]
        notebook-sniffer (get-in db path-sniffer)]
    (if notebook-current
      [path-current notebook-current]
      (if notebook-sniffer
        [path-sniffer notebook-sniffer]
        (let [notebook-new (create-notebook)]
          (add-notification :danger (str "sniffer: adding evals to notebook: " id-doc-sniffer))
          [path-sniffer notebook-new])))))

(reg-event-db
 :sniffer/rcvd
 (fn [db [_ msg]]
   (info "sniffer rcvd: " msg)
   (let [{:keys [session-id-sink session-id-source]} msg]
     (if (or session-id-sink session-id-source)
       ; admin message
       db
       ; eval or eval result
       (let [[path notebook] (get-notebook db)]
         (cond
           (= "eval" (:op msg))
           (assoc-in db path (add-code-segment notebook msg))
           :else
           (assoc-in db path (add-result notebook msg))))))))


