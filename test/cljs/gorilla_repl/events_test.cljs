(ns gorilla-repl.events-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [re-frame.core :as reframe]
            [re-frame.db :as db]
            [reagent.core :as reagent :refer [atom]]
            [gorilla-repl.events :as gev]
            [gorilla-repl.db :as gdb]
            [gorilla-repl.core :as grc]))

(defn my-handler
  [db [_ v]]
  (assoc db :foo "bar"))

(reframe/reg-event-db
  :initialise-test-db
  (fn [_ [_]]
    gdb/initial-db))

#_(reframe/reg-event-db
  :some-id
  [gev/standard-interceptors]
  my-handler)

(defn my-test-fixture [f]
  (reframe/dispatch-sync [:initialise-test-db])
  (f)
  ;; (destroy-db)
  )

(use-fixtures :once my-test-fixture)


(deftest test-event
  (testing "Samples"
    (testing "Initial DB"
      (is (get @db/app-db :all-commands)))
    (testing "Save as change"
      (let [filename "foo.clj"]
        (reframe/dispatch-sync [:save-as-change filename])
        (is (= filename (get-in @db/app-db [:save :filename])))))))
