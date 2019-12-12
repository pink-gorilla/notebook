(ns pinkgorilla.events.helper
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [re-frame.core :refer [after debug dispatch]]
   [pinkgorilla.db :as db]))

(defn text-matches-re
  [val item]
  (let [res (str/join ".*" (str/split (str/lower-case val) #""))
        re (re-pattern (str res ".*"))]
    (re-matches re (str/lower-case (:text item)))))

(defn default-error-handler
  [{:keys [status status-text]}]
  (dispatch [:process-error-response "default" status status-text]))


;; -- Interceptors  --------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/wiki/Using-Handler-Middleware
;;


(defn check-and-throw
  "throw an exception if db doesn't match the spec."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

;; Event handlers change state, that's their job. But what happens if there's
;; a bug which corrupts db state in some subtle way? This interceptor is run after
;; each event handler has finished, and it checks app-db against a spec.  This
;; helps us detect event handler bugs early.
(def check-spec-interceptor (after (partial check-and-throw :pinkgorilla.db/db)))

;; the chain of interceptors we use for all handlers that manipulate data
(def standard-interceptors [(when db/debug? debug)          ;; look in your browser console for debug logs
                            check-spec-interceptor          ;; ensure the spec is still valid
                            #_trim-v])                      ;; removes first (event id) element from the event vec
