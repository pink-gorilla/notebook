(ns pinkgorilla.events.usage-analytics
  (:require
   [re-frame.core :refer [reg-fx]]
   ;[district0x.re-frame.google-analytics-fx]
   ))

(def ^:dynamic *enabled* true)

(defn set-enabled! [enabled?]
  (set! *enabled* enabled?))

;; register a co-effect handler

;; https://developers.google.com/analytics/devguides/collection/gtagjs/migration

(reg-fx
 :ga/event
 (fn [[category]]
   (when *enabled*
     (js/gtag "event" (name category)); label value (clj->js fields-object)
     )))

