(ns gorilla-repl.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [gorilla-repl.core-test]
            [gorilla-repl.events-test]))

(doo-tests 'gorilla-repl.core-test
           'gorilla-repl.events-test)
