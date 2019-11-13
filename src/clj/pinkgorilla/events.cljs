(ns pinkgorilla.events
  (:require
   [clojure.spec.alpha :as s]
    ;; [cemerick.url :as url]
   [clojure.string :as str]
   [goog.crypt.base64 :as b64]
   [clojure.walk :as w]
   [taoensso.timbre :as timbre
    :refer-macros (log trace debug info warn error fatal report
                       logf tracef debugf infof warnf errorf fatalf reportf
                       spy get-env log-env)]
   [ajax.core :as ajax :refer [GET POST]]
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx path trim-v after debug dispatch dispatch-sync]]
   [day8.re-frame.http-fx]
   [day8.re-frame.undo :as undo :refer [undoable]]

   [cljsjs.mousetrap]
   [cljsjs.mousetrap-global-bind]

   [pinkgorilla.db :as db :refer [initial-db]]
   [pinkgorilla.editor.core :as editor]
   [pinkgorilla.kernel.nrepl :as nrepl]
   [pinkgorilla.routes :as routes]
   [pinkgorilla.notebook.newnb :refer [create-new-worksheet]]

   [pinkgorilla.events.helper :refer [text-matches-re default-error-handler  check-and-throw  standard-interceptors]]
   [pinkgorilla.events.worksheet]
   [pinkgorilla.events.persistence]
   [pinkgorilla.events.palette]
   [pinkgorilla.events.kernel]
   [pinkgorilla.events.uiload]
   [pinkgorilla.events.message]
   [pinkgorilla.events.doc]
   [pinkgorilla.events.settings]
   [pinkgorilla.events.config]
   [pinkgorilla.events.multikernel]))


;; http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj
;; http://localhost:3449/worksheet.html#/view?source=gist&id=2c210794185e9d8c0c80564db581b136&filename=new-render.clj

;; -- the register of event handlers --------------------------------------------------------------

;; -- Helpers -----------------------------------------------------------------

#_(defn allocate-next-id
    "Returns the next todo id.
    Assumes todos are sorted.
    Returns one more than the current largest id."
    [todos]
    ((fnil inc 0) (last (keys todos))))




;; TODO : Remove all the side effects from the handlers
;; TODO : require/loading should not be used for setup
;; -- Event Handlers ----------------------------------------------------------


(defn- view-db
  [app-url]
  (let [base-path (str/replace (:path app-url) #"[^/]+$" "")
        db (merge initial-db {:base-path base-path})]
    db))


(reg-event-db
 :initialize-view-db
 (fn [_ [_ app-url]]
   (view-db app-url)))

(reg-event-db
 :initialize-new-worksheet
 (fn [db _]
   (assoc db :worksheet (create-new-worksheet))))

(reg-event-db
 :worksheet:segment-clicked
 (fn [db [_ segment-id]]
   (assoc-in db [:worksheet :active-segment] segment-id)))

(reg-event-db
 :segment-value-changed
 (fn [db [_ seg-id value]]
   (assoc-in db [:worksheet :segments seg-id :content :value] value)))


;; TODO Should move evaluation state out of worksheet
(undo/undo-config! {:max-undos    3
                    :harvest-fn   (fn [ratom] (some-> @ratom :worksheet))
                    :reinstate-fn (fn [ratom value] (swap! ratom assoc-in [:worksheet] value))})
