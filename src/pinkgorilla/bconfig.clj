(ns pinkgorilla.bconfig
  (:require
   [clojure.edn :as edn]
   [com.rpl.specter :as s]
   [fipp.clojure]))

(defn pr-str-fipp [config]
  (with-out-str
    (fipp.clojure/pprint config {:width 40})))

(defn generate-bundle-config [& args]
  (let [base  (-> (slurp "deps.edn") (edn/read-string))
        bundel (get-in base [:aliases :notebook :extra-deps])]
    (->> (s/transform [:deps] #(merge % bundel) base)
         (pr-str-fipp)
         (spit "bundel/deps.edn"))))

(comment
  (generate-bundle-config)

  ;
  )
