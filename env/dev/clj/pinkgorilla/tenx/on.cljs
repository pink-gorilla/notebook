(ns pinkgorilla.tenx.on
  "reframe events related to 10x

   this ns gets changed in in shadow cljs config with
   :ns-aliases to pinkgorilla.tenx.config

  note: calling ns 10x does lead to errors (at runtime in the bundle) "
  (:require
   ;[taoensso.timbre :refer-macros (info)]
   [re-frame.core :refer [reg-event-db]]
   [day8.re-frame-10x]
   [clojure.string]))

(def use-10x true)

(defn has-10x? []
  true)

(defn configure-10x! []
  (println "Adding 10x event handlers ..")

  (println "hiding re-frame-10x panel")
  (.setItem js/localStorage "day8.re-frame-10x.show-panel" "false")

  (reg-event-db
   :toggle.reframe10x
   (fn [db _]
     (let [visible (not (get-in db [:dev :reframe10x-visible?]))
           _ (println "reframe-10x visible: " visible)
        ; _ (.setItem js/localStorage "day8.re-frame-10x.show-panel" (str visible))
           _ (day8.re-frame-10x/show-panel! visible)] ; https://github.com/day8/re-frame-10x/pull/210s
       (assoc-in db [:dev :reframe10x-visible?] visible)))))