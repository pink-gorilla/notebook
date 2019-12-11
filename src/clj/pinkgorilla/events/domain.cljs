(ns pinkgorilla.events.domain
  (:require 
   [re-frame.core :refer [reg-cofx]]
   #_[cemerick.url :as url]))


;; from 
;; https://github.com/timgilbert/haunting-refrain-posh/blob/develop/src/cljs/haunting_refrain/fx/domain.cljs

(defn- current-origin-coeffect
  "Coeffect which figures out what dns origin the app is currently running on and associates
  it with the :domain coeffect."
  [coeffects]
  (assoc coeffects :origin (.. js/window -location -origin)))

(reg-cofx :origin current-origin-coeffect)