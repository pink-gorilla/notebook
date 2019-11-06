(ns pinkgorilla.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [pinkgorilla.core-test]
            [pinkgorilla.events-test]))

(doo-tests 'pinkgorilla.core-test
           'pinkgorilla.events-test)
