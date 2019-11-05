(ns gorilla-repl.cards
  (:require 
    [cljs.test :refer-macros [is testing]]
    [reagent.core :as reagent :refer [atom]]
    ;; [reagent.session :as session]
    [pinkgorilla.core :as core])
  (:require-macros
    [devcards.core
       :as dc
       :refer [defcard defcard-doc defcard-rg deftest]]))

;; https://8thlight.com/blog/eric-smith/2016/10/05/a-testable-clojurescript-setup.html
;; rlwrap lein run -m clojure.main script/figwheel.clj
;; http://localhost:3449/devcards.html

(defcard-rg first-card
            [:div>h1 "This is your first devcard!"])

(deftest a-test
         (testing "FIXME, I fail."
           (is (= 0 1))))

#_(defcard-rg home-page-card
              [core/home-page])

#_(reagent/render [:div] (.getElementById js/document "app"))
