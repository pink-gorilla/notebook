(ns pinkgorilla.karma-runner
  (:require [jx.reporter.karma :as karma :include-macros true]
    ;; [pinkgorilla.webpack-bundle]
            [pinkgorilla.reframe-test]))

(enable-console-print!)

(defn ^:export run-karma [karma]
  (karma/run-tests
    karma
    'pinkgorilla.reframe-test))
