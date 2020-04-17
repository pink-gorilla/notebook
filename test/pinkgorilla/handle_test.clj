(ns pinkgorilla.handle-test
  (:require
   [clojure.test :refer [is deftest run-tests]]
   [ring.mock.request :as mock]
   [pinkgorilla.route]))

;; TODO: How about the repl websocket handler?


#_(defn mock-save-notebook
    [file data])

#_(defn mock-load-notebook
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
              (mock/request :get "/pink-gorilla-32.png"))
        status (:status resp)
        headers (:headers resp)
        content-type (get headers "Content-Type")]
    (is (= 200 status))
    (is (= "image/png" content-type))))

(deftest default-handler-save-test
  (let [resp (#'pinkgorilla.route/default-handler
              (mock/request :post "/save" {:notebook    ";; gorilla-repl.fileformat = 2\n"
                                           :storagetype "file"
                                           :filename    "target/test-save.cljg"
                                            ;; :tokens[default-kernel]: "clj"
                                            ;; :tokens[editor]: "text"
                                            ;; :tokens[github-token]: ""
                                           }))
        status (:status resp)
        headers (:headers resp)
        content-type (get headers "Content-Type")]
    (is (= 200 status))
    (is (= "application/json; charset=utf-8" content-type))))

(deftest default-handler-load-test
  ;;(with-redefs [storage-handle/load-notebook mock-load-notebook])
  (let [resp (#'pinkgorilla.route/default-handler
              (mock/request :get "/load?filename=./test/notebooks/broken/reagent-demo-err-renderex.cljg&storagetype=file&tokens[default-kernel]=clj&tokens[editor]=text&tokens[github-token]="))
        status (:status resp)
        headers (:headers resp)
        content-type (get headers "Content-Type")]
    (is (= 200 status))
    (is (= "application/json; charset=utf-8" content-type))))

#_(run-all-tests)
(run-tests)

#_(.addShutdownHook
   (Runtime/getRuntime)
   (proxy [Thread] []
     (run []
       (run-all-tests))))
