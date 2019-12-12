(ns pinkgorilla.output.reagent-cljs
  (:require
   [taoensso.timbre :refer-macros (info)]
   [clojure.edn :as edn]
   [pinkgorilla.ui.pinkie :refer [resolve-functions]]))

(defn output-reagent-cljs
  [output _]
  (let [map-keywords (:map-keywords output)
        component (:reagent output)
        _ (info "map-keywords: " map-keywords " reagent component: " component)
        component (if map-keywords (resolve-functions component) component)]
    component))

(defn output-reagent-cljs-from-clj
  ""
  [output _]
  (let [r (edn/read-string (:value output))]
    (output-reagent-cljs {:map-keywords true :reagent r} _)))


