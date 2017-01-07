(ns gorilla-repl.karma-runner
  (:require [jx.reporter.karma :as karma :include-macros true]
            [gorilla-repo.webpack-bundle]
            [gorilla-repl.test-test]))

(enable-console-print!)

(defn ^:export run-karma [karma]
  (karma/run-tests
    karma
    'gorilla-repl.test-test))
