(ns pinkgorilla.handle-test
  (:require [ring.mock.request :as mock]
            [clojure.java.io :as io]
            [pinkgorilla.handle :as handle]
            [pinkgorilla.route :refer :all])
  (:use clojure.test))

;; TODO: How about the repl websocket handler?

(defn mock-persist
  [file data])

(defn mock-read-sheet-locally
  [ws-file]
  (slurp (io/resource ws-file) :encoding "UTF-8"))

#_(defn my-test-fixture [f]
    (f))

;; (use-fixtures :once my-test-fixture)

(deftest not-found-test
  (is (= (#'pinkgorilla.route/default-handler (mock/request :get "/404"))
         {:status  404
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :body    "Bummer, not found"}))
  (let [resp (#'pinkgorilla.route/default-handler (mock/request :get "/gorilla-files"))
        status (:status resp)]
    (is (= 200 status))))

(deftest files-test
  (let [resp (#'pinkgorilla.route/default-handler
               (mock/request :get "/gorilla-files"))
        status (:status resp)
        headers (:headers resp)
        content-type (get headers "Content-Type")]
    (is (= 200 status))
    (is (= "application/json; charset=utf-8" content-type))))

(deftest config-test
  (let [resp (#'pinkgorilla.route/default-handler (mock/request :get "/config"))
        status (:status resp)
        headers (:headers resp)
        content-type (get headers "Content-Type")]
    (is (= 200 status))
    (is (= "application/json; charset=utf-8" content-type))))

(deftest document-test
  (let [resp (#'pinkgorilla.route/default-handler (mock/request :get "/worksheet.html"))
        status (:status resp)
        headers (:headers resp)
        cookie (get headers "Set-Cookie")
        content-type (get headers "Content-Type")]
    (is cookie)
    (is (= 200 status))
    (is (= "text/html; charset=utf-8" content-type))))

(deftest resource-test
  (let [resp (#'pinkgorilla.route/default-handler
               (mock/request :get "/favicon.ico"))
        status (:status resp)
        headers (:headers resp)
        content-type (get headers "Content-Type")]
    (is (= 200 status))
    (is (= "image/x-icon" content-type))))

(deftest default-handler-save-test
  (with-redefs [handle/persist mock-persist]
    (let [resp (#'pinkgorilla.route/default-handler
                 (mock/request :post "/save" {:worksheet-data     "dummy"
                                              :worksheet-filename "foobar.clj"}))
          status (:status resp)
          headers (:headers resp)
          content-type (get headers "Content-Type")]
      (is (= 200 status))
      (is (= "application/json; charset=utf-8" content-type)))))

(deftest default-handler-load-test
  (with-redefs [handle/read-sheet-locally mock-read-sheet-locally]
    (let [resp (#'pinkgorilla.route/default-handler
                 (mock/request :get "/load?worksheet-filename=pinkgorilla%2Fhandle_test.clj"))
          status (:status resp)
          headers (:headers resp)
          content-type (get headers "Content-Type")]
      (is (= 200 status))
      (is (= "application/json; charset=utf-8" content-type)))))



#_(run-all-tests)
(run-tests)

#_(.addShutdownHook
    (Runtime/getRuntime)
    (proxy [Thread] []
      (run []
        (run-all-tests))))
