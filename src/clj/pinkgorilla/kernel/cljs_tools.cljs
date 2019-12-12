(ns pinkgorilla.kernel.cljs-tools
  (:require
   [clojure.string :as str]
   [pinkgorilla.ui.gorilla-renderable :refer [Renderable render]]
   [taoensso.timbre :refer-macros (info)]
   [shadow.cljs.bootstrap.env]))

(defn r! [vec_or_reagent_f]
 ; "renders a (hydrated) reagent component"
  (reify Renderable
    (render [_]
      {:type :reagent-cljs
       :content {} ; reagent components cannot get persisted - they are living functions compiled in the notebook
       :reagent vec_or_reagent_f
             ;:value result
       })))

(defn print-loaded-shadow-namespaces []
  (let [lns  @shadow.cljs.bootstrap.env/loaded-ref
        lns (map name lns)
        lns (remove #(str/starts-with? % "goog") lns)
        lns (remove #(str/starts-with? % "module$node_modules") lns)
        lns (remove #(str/starts-with? % "day8.re-frame-10x.") lns)
        lns (sort lns)]
    (info "loaded shadow namespaces: " (pr-str lns))))


