
(ns pinkgorilla.notebook-ui.schema.interceptor
  (:require
   [clojure.string :as str]
   [taoensso.timbre :as timbre :refer [info error]]
   [clojure.spec.alpha :as s]
   [re-frame.core :refer [after debug reg-global-interceptor reg-event-fx]]
   [pinkgorilla.notebook-ui.schema.schema :as db]))

(def debug?
  ^boolean js/goog.DEBUG)

(defn check-and-throw
  "throw an exception if db doesn't match the spec."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (error "spec error: " (s/explain-data a-spec db))
    (throw (ex-info (str "spec check failed: "
                         (s/explain-data a-spec db)) {}))))

(defn check-for-empty
  [db]
  (when (or (not db)
            (not (map? db)))
    (error "db is nil or not a map!")
    (throw (ex-info  "db is nil or not a map!" {}))))

;; Event handlers change state, that's their job. But what happens if there's
;; a bug which corrupts db state in some subtle way? This interceptor is run after
;; each event handler has finished, and it checks app-db against a spec.  This
;; helps us detect event handler bugs early.

(def check-spec-interceptor
  (after
   check-for-empty
   ;(partial check-and-throw :pinkgorilla.notebook-ui.schema/db)
   ))

;; the chain of interceptors we use for all handlers that manipulate data
(def standard-interceptors [(when debug? debug)          ;; look in your browser console for debug logs
                            check-for-empty
                            ;check-spec-interceptor          ;; ensure the spec is still valid
                            #_trim-v])                      ;; removes first (event id) element from the event vec

(reg-event-fx
 :schema/check-on
 (fn [_ _]
   (info ":schema/scheck-on")
   (reg-global-interceptor check-spec-interceptor)
   nil))

