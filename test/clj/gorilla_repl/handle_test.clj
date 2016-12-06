(ns gorilla-repl.handle-test
  (:require [ring.mock.request :as mock]
            [gorilla-repl.route :refer :all])
  (:use clojure.test))

#_(defn my-test-fixture [f]
  (f))

;; (use-fixtures :once my-test-fixture)

(deftest handler-test
  (is (= (#'gorilla-repl.route/default-handler (mock/request :get "/404"))
         {:status  404
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :body    "Bummer, not found"})))

#_(run-all-tests)
(run-tests)

#_(.addShutdownHook
  (Runtime/getRuntime)
  (proxy [Thread] []
    (run []
      (run-all-tests))))
