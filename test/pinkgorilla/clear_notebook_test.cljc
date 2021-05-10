(ns pinkgorilla.clear-notebook-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])
   [pinkgorilla.document.default-config] ; side-effects
   [pinkgorilla.notebook-ui.hydration :refer [clear-all clear-active]]))

(def notebook
  {:meta {:tagline "test"}
   :segments {1 {:code "(+ 1 1)" :picasso "xxx"}
              2 {:code "(+ 2 2)" :picasso "xxx"}}
   :order [1 2]
   :active 1})

(def notebook-clear-active
  {:meta {:tagline "test"}
   :segments {1 {:code "(+ 1 1)"}
              2 {:code "(+ 2 2)" :picasso "xxx"}}
   :order [1 2]
   :active 1})

(def notebook-clear-all
  {:meta {:tagline "test"}
   :segments {1 {:code "(+ 1 1)"}
              2 {:code "(+ 2 2)"}}
   :order [1 2]
   :active 1})

#?(:clj
   (deftest clear-notebook
     (is (= notebook-clear-active (clear-active notebook)))
     (is (= notebook-clear-all (clear-all notebook))))
;   
   )


