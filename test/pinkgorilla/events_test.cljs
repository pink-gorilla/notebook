(ns pinkgorilla.events-test
  (:require
   [cljs.test :refer-macros [is deftest testing use-fixtures]]
   [re-frame.core :as reframe]
   [re-frame.db :as db]
   [pinkgorilla.events]
   [pinkgorilla.db :as gdb]))

(defn my-handler
  [db [_ _]]
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
    (testing "Save Notebook as file"
      (let [filename "foo.cljg"]
        (reframe/dispatch-sync [:save-as-storage {:source :file :filename filename}])
        (is (= filename (get-in @db/app-db [:storage :filename])))))))
