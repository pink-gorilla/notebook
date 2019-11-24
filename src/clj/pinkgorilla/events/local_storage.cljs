(ns pinkgorilla.events.local-storage)

;; https://github.com/timgilbert/haunting-refrain-posh/blob/develop/src/cljs/haunting_refrain/fx/local_storage.cljs



(comment

  (ns haunting-refrain.fx.local-storage
    (:require [hodgepodge.core :as hp]
              [re-frame.core :refer [reg-cofx reg-fx]]))

  (defn local-storage-coeffect
    "Coeffect which retrieves an object out of localStorage under the given key and associates
  it with the :local-storage coeffect."
    [coeffects key]
    (assoc coeffects :local-storage (get hp/local-storage key)))
  (reg-cofx :local-storage local-storage-coeffect)

  (defn persist-effect
    "Persist an object into localStorage at the given key."
    [[key value]]
    (assoc! hp/local-storage key value))
  (reg-fx :persist! persist-effect)
  
  )
