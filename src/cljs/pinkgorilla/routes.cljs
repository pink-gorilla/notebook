(ns pinkgorilla.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import
     [goog History]
     ;; [goog.history Html5History]
      )
  (:require
     [secretary.core :as secretary]
     [goog.events :as events]
     [goog.history.EventType :as EventType]
     [re-frame.core :as re-frame]))

;; Fix browser/History URL http://www.lispcast.com/mastering-client-side-routing-with-secretary-and-goog-history
#_(defn get-token []
    (str js/window.location.pathname js/window.location.search))

#_(defn make-history []
    (doto (Html5History.)
      (.setPathPrefix (str js/window.location.protocol
                           "//"
                           js/window.location.host))
      (.setUseFragment false)))

#_(defn handle-url-change [e]
    ;; log the event object to console for inspection
    (js/console.log e)
    ;; and let's see the token
    (js/console.log (str "Navigating: " (get-token)))
    ;; we are checking if this event is due to user action,
    ;; such as click a link, a back button, etc.
    ;; as opposed to programmatically setting the URL with the API
    (when-not (.-isNavigation e)
      ;; in this case, we're setting it
      (js/console.log "Token set programmatically")
      ;; let's scroll to the top to simulate a navigation
      (js/window.scrollTo 0 0))
    ;; dispatch on the token
    (secretary/dispatch! (get-token)))

#_(defonce history (doto (make-history)
                     (goog.events/listen EventType.NAVIGATE
                                         ;; wrap in a fn to allow live reloading
                                         #(handle-url-change %))
                     (.setEnabled true)))

#_(defn nav! [token]
    (.setToken history token))

(defn hook-browser-navigation!
  []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defonce history (hook-browser-navigation!))

(defn nav!
  [token]
  (.setToken history token))

(defn app-routes
  [& [{:keys [hook-navigation]
       :or   {hook-navigation false}}]]
  (secretary/set-config! :prefix "#")
  (defroute "/new" [query-params]
            (re-frame/dispatch [:initialize-new-worksheet]))
  (defroute "/edit" [query-params]
            (re-frame/dispatch [:edit-file (:worksheet-filename query-params)]))
  (defroute "/view" [query-params]
            (re-frame/dispatch [:view-file query-params]))
  (defroute "/reset" []
             (nav! "/new")))
