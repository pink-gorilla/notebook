(ns pinkgorilla.prod
  (:require 
    [pinkgorilla.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
