(ns pinkgorilla.karma-runner
  (:require [jx.reporter.karma :as karma :include-macros true]
    ;; [pinkgorilla.webpack-bundle]
            [pinkgorilla.test-test]))

(enable-console-print!)

(defn ^:export run-karma [karma]
  (karma/run-tests
    karma
    'pinkgorilla.test-test))
