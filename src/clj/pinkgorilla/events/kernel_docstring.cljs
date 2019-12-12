(ns pinkgorilla.events.kernel-docstring
  (:require
   [re-frame.core :refer [reg-event-db]]
   [pinkgorilla.events.helper :refer [standard-interceptors]]
   [pinkgorilla.kernel.core :as kernel]))

(reg-event-db
 :show-doc
 (fn [db [_ doc]]
   (assoc-in db [:docs] {:content doc})))

(reg-event-db
 :hide-doc
 (fn [db _]
   (assoc-in db [:docs :content] "")))

(reg-event-db
 :docs:clojuredocs
 [standard-interceptors]
 (fn [db [_ win token]]
   ; TODO: awb99 :clj is a hack - dont know where to get current cell kernel
   (kernel/resolve-symbol :clj token
                          (get-in db [:worksheet :ns])
                          (fn [msg]
                            (if-let [ns (:ns msg)]
                              (set! (.-location win) (str "http://clojuredocs.org/clojure_core/" ns "/" (get msg "symbol")))
                              (set! (.-location win) (str "http://clojuredocs.org/search?q=" token)))))))
