(ns pinkgorilla.core-test
  (:require
   [clojure.test :refer [is deftest run-tests]]
  ;; the following requires are needed to bring the tests into scope
   [pinkgorilla.handle-test]
   [pinkgorilla.nrepl-specs-test]
   [pinkgorilla.reframe-test]))


(run-tests)

#_(.addShutdownHook
   (Runtime/getRuntime)
   (proxy [Thread] []
     (run []
       (run-all-tests))))
