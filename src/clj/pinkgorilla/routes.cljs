(ns pinkgorilla.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import
   [goog History]
    ;; [goog.history Html5History]
   )
  (:require
   [taoensso.timbre :refer-macros (info debug)]
   [secretary.core :as secretary]
   [goog.events :as events]
   [goog.history.EventType :as EventType]
   [re-frame.core :as re-frame]
   [pinkgorilla.explore.tags :refer [tags-csv->list]]))

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

(defn   nav!
  "navigates the broser to the url. 
   Triggers secretary route events"
  [url]
  (.setToken history url))

(defn set-hash!
  "changes the displayed URL in the browser, but does not cause the browser 
   to navigate to this url. This does not trigger secretary events."
  [hash]
  (let [location (.-location js/window)
        ;protocol (.-protocol location) ; "http:"
        ;hostname (.-hostname location) ;  localhost
        ;port (.-port location) ; 9000
        ;host (.-host location) ; "localhost:9000"
        origin (.-origin location) ; "http://localhost:9000"
        pathname (.-pathname location) ;  "/worksheet.html"
        ;hash (.-hash location) ; "#/edit?source=file&filename=../../quant/backtest/notebooks/bongo-test.cljg"
        ;href (.-href location) ;  "http://localhost:9000/worksheet.html#/edit?source=file&filename=../../quant/backtest/notebooks/bongo-test.cljg"
        url-new (str origin pathname "#" hash)]
    ;(println "set-hash! url:" url-new)
    (debug "set-hash! url:" url-new)
    (set! (.-location js/window) url-new)))

(defn app-routes
  [& [{:keys [hook-navigation]
       :or   {hook-navigation false}}]]
  (info "Hook navigation" hook-navigation)
  (secretary/set-config! :prefix "#")
  (defroute "/new" []
    (re-frame/dispatch [:initialize-new-worksheet]))
  (defroute "/edit" [query-params]
    (when query-params
      (re-frame/dispatch [:edit-file query-params])))
  (defroute "/view" [query-params]
    (re-frame/dispatch [:view-file query-params]))
  (defroute "/reset" []
    (nav! "/new")))

(defroute projects-path "/explore" [query-params]
  (info "navigated to /explore")
  (re-frame/dispatch [:explorer-show (set (tags-csv->list (:tags query-params)))]))

(defroute renderer-path "/renderer" [query-params]
  (info "navigated to /renderer")
  (re-frame/dispatch [:renderer-show]))



;; TODO: oauth callbacks
;   {:route/url  "/foursquare-hello"
;    :route/page foursquare/hello-page}
