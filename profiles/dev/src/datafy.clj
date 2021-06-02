(ns demo.datafy
  "Sample code demonstrating naving around a random graph of data.
   The graph very likely will have circular references, which is not a problem.
   To see the results, execute the entire file.
   Each step in the nav process will be printed out, as well as the initial db.
   Subsequent executions will generate a new random db."
  (:require [clojure.datafy :refer [datafy nav]]))

(defn generate-db
  "Generate a random database of users and departments.
   It is ENTIRELY possible for this database to contain
   circular links, but that is not a problem with the lazy
   nature of datafy/nav."
  []
  (let [user-ids (take 5 (shuffle (range 100)))
        department-ids (take 3 (shuffle (range 100)))
        new-user (fn [id]
                   (let [department-id (rand-nth department-ids)
                         manager-id (rand-nth user-ids)]
                     {:id id
                      :name (str "User " id)
                      :department-id department-id
                      :manager-id manager-id}))
        new-department (fn [id]
                         (let [deptartment-head-id (rand-nth user-ids)]
                           {:id id
                            :name (str "Department " id)
                            :department-head-user-id deptartment-head-id}))]
    {:users (zipmap user-ids (map new-user user-ids))
     :departments (zipmap department-ids (map new-department department-ids))}))

(comment
  ;sample generated db
  {:users
   {65 {:id 65, :name "User 65", :department-id 96, :manager-id 58}
    58 {:id 58, :name "User 58", :department-id 85, :manager-id 58}}
   :departments
   {96 {:id 96, :name "Department 96", :department-head-user-id 65}
    85 {:id 85, :name "Department 85", :department-head-user-id 65}}})


(defn lookup-user
  "Convenience function to get user by id"
  [db id]
  (get-in db [:users id]))

(defn lookup-department
  "Convenience function to get department by id"
  [db id]
  (get-in db [:departments id]))

(declare datafy-ready-user)
(declare datafy-ready-department)

(defn navize-user
  [db user]
  (when user
    (with-meta
      user
      {'clojure.core.protocols/nav (fn [coll k v]
                                     (case k
                                      ;; by returning a datafy-ed object,
                                      ;; the db is propagated to further datafy/nav calls
                                       :manager-id (datafy-ready-user db (lookup-user db v))
                                       :department-id (datafy-ready-department db (lookup-department db v))
                                       v))})))

(defn navize-department
  [db department]
  (when department
    (with-meta
      department
      {'clojure.core.protocols/nav (fn [coll k v]
                                     (case k
                                       :department-head-user-id (datafy-ready-user db (lookup-user db v))
                                       v))})))

(defn datafy-ready-user
  [db user]
  (when user
    (with-meta
      user
      {'clojure.core.protocols/datafy (fn [x] (navize-user db x))})))

(defn datafy-ready-department
  [db department]
  (when department
    (with-meta
      department
      {'clojure.core.protocols/datafy (fn [x] (navize-department db x))})))

;--------------------------------------------------------------
; The rest of the code creates the db and navs around the graph
;--------------------------------------------------------------

(def user1
  (let [db (generate-db)
        _ (clojure.pprint/pprint db)
        ;; grab a random user-id from which to start nav-ing
        user-id (-> db :users keys first)
        user (lookup-user db user-id)]
    (println "\nuser1:" user "\n")

    ;; this step kicks off the datafy/nav process
    (datafy (datafy-ready-user db user))

    ;; just for fun, uncomment this and see what happens
    ;; if a plain (non-datafy-ready object) is used
    #_(datafy user)))

;; new let-block, notice db is out of scope,
;; but navigating the graph is still possible
(let [nav-to-link (fn [record k]
                    (println "nav-ing to" (name k) ":" (get record k))
                    (when record
                      (datafy (nav record k (get record k)))))

      user2 (nav-to-link user1 :manager-id)
      _ (println "user2:" user2 "\n")

      user3 (nav-to-link user2 :manager-id)
      _ (println "user3:" user3 "\n")

      ;; nav-ing to :name works, but ends up at leaf in graph
      name3 (nav-to-link user3 :name)
      _ (println "name3:" name3 "\n")

      department3 (nav-to-link user3 :department-id)
      _ (println "department3:" department3 "\n")

      user4
      (nav-to-link department3 :department-head-user-id)
      _ (println "user4:" user4 "\n")])


; Sample output:
;{:users
; {65 {:id 65, :name "User 65", :department-id 96, :manager-id 9},
;  22 {:id 22, :name "User 22", :department-id 85, :manager-id 65},
;  56 {:id 56, :name "User 56", :department-id 85, :manager-id 56},
;  9 {:id 9, :name "User 9", :department-id 96, :manager-id 65},
;  58 {:id 58, :name "User 58", :department-id 85, :manager-id 58}},
; :departments
; {96 {:id 96, :name "Department 96", :department-head-user-id 65},
;  9 {:id 9, :name "Department 9", :department-head-user-id 58},
;  85 {:id 85, :name "Department 85", :department-head-user-id 65}}}
;
;user1: {:id 65, :name User 65, :department-id 96, :manager-id 9}
;
;nav-ing to manager-id : 9
;user2: {:id 9, :name User 9, :department-id 96, :manager-id 65}
;
;nav-ing to manager-id : 65
;user3: {:id 65, :name User 65, :department-id 96, :manager-id 9}
;
;nav-ing to name : User 65
;name3: User 65
;
;nav-ing to department-id : 96
;department3: {:id 96, :name Department 96, :department-head-user-id 65}
;
;nav-ing to department-head-user-id : 65
;user4: {:id 65, :name User 65, :department-id 96, :manager-id 9}