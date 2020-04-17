(ns pinkgorilla.core-test
  (:require
   [clojure.test :refer [is deftest run-tests]]
  ;; the following requires are needed to bring the tests into scope
   [pinkgorilla.handle-test]
   [pinkgorilla.nrepl-specs-test]
   [pinkgorilla.reframe-test]))

(defn my-test-fixture [f]
  (f))

;; (use-fixtures :once my-test-fixture)

(defn add [x y] (+ x y))

(deftest add-x-to-y-a-few-times
  (is (= 5 (add 2 3)))
  (is (= 5 (add 1 4)))
  (is (= 5 (add 3 2))))

;; (run-all-tests)
;; 
(run-tests)

#_(.addShutdownHook
   (Runtime/getRuntime)
   (proxy [Thread] []
     (run []
       (run-all-tests))))
