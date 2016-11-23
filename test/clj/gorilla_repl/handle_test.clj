(ns gorilla-repl.handle-test
  (:require [ring.mock.request :as mock]
            [gorilla-repl.core :refer :all])
  (:use clojure.test))

#_(defn my-test-fixture [f]
  (f))

;; (use-fixtures :once my-test-fixture)

(deftest handler-test
  (is (= (#'gorilla-repl.core/dev-routes (mock/request :get "/404"))
         {:status  404
          :headers {"content-type" "text/html"}
          :body    "Bummer, not found"})))

#_(run-all-tests)
(run-tests)

#_(.addShutdownHook
  (Runtime/getRuntime)
  (proxy [Thread] []
    (run []
      (run-all-tests))))
