(ns pinkgorilla.hydration-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])))

(def notebook
  {:meta {:format 3
          :author "awb99"
          :id "abcd-1234-5678"
          :date "2019-11-08 09:40:00Z"
          :tags [:super :vega :ui :sample]}
   :segments [{:type :md :input "#hello"}
              {:type :code :kernel :clj :input "(+ 1 2)" :output "3"}
              {:type :code :kernel :cljs :input "(+ 1 2)" :output "3"}
              {:type :code :kernel :mock :input "(+ 1 2)" :output "Everything is wunderbar"}]})

(deftest hydration
  (is (= 4 (count (:segments notebook)))))