(ns gorilla-repl.prod
  (:require [gorilla-repl.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
