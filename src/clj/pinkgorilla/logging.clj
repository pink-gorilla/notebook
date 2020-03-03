(ns pinkgorilla.logging
  (:import [ch.qos.logback.classic Logger]
           [ch.qos.logback.classic Level]))


;; stolen from:
;; https://github.com/techascent/tech.ml.dataset/blob/master/src/tech/ml/utils/slf4j_log_level.clj


(defn set-log-level
  [log-level]
  (.setLevel
   (org.slf4j.LoggerFactory/getLogger
    (Logger/ROOT_LOGGER_NAME))
   (case log-level
     :all Level/ALL
     :debug Level/DEBUG
     :trace Level/TRACE
     :info Level/INFO
     :warn Level/WARN
     :error Level/ERROR
     :off Level/OFF))
  log-level)