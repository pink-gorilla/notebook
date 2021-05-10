(ns pinkgorilla.core-test
  (:require
   [clojure.test :refer [deftest is testing]]))

(deftest dummy
  (testing "dummy test"
    (is (= [1 2] [1 2]))))

(comment
  (bidi/path-for demo-routes-api :demo/main)
  (bidi/path-for demo-routes-api :ui/explorer)
  (bidi/path-for demo-routes-api :api/explorer)

  (bidi.bidi/match-route demo-routes-api "/explorer")
  (bidi.bidi/match-route demo-routes-api "/api/explorer" :request-method :get)

  (require '[clojure.java.io :as io])
  (.getFile (io/resource "public/index.html")))


