(ns pinkgorilla.kernel.nrepl-specs
  (:require  [clojure.spec.alpha :as s]))

(println "adding nrepl specs...")

(s/def :nrepl-msg/ns clojure.core/string?)
(s/def :nrepl-msg/fn clojure.core/string?)
(s/def :nrepl-msg/method clojure.core/string?)

(s/def :nrepl-msg/name clojure.core/string?)
(s/def :nrepl-msg/var clojure.core/string?)
(s/def :nrepl-msg/class clojure.core/string?)

(s/def :nrepl-msg/flags (s/coll-of clojure.core/keyword? :kind clojure.core/set?))
(s/def :nrepl-msg/type #{:java :clj})

; origin of source
(s/def :nrepl-msg/file clojure.core/string?)
(s/def :nrepl-msg/line clojure.core/integer?)
(s/def :nrepl-msg/file-url (s/nilable clojure.core/string?))

(s/def :nrepl-msg/stacktrace
  (s/coll-of
   (s/keys :req-un [:nrepl-msg/class
                    :nrepl-msg/file
                    :nrepl-msg/file-url
                    :nrepl-msg/flags
                    :nrepl-msg/line
                    :nrepl-msg/method
                    :nrepl-msg/name
                    :nrepl-msg/type]
           :opt-un [:nrepl-msg/ns
                    :nrepl-msg/fn
                    :nrepl-msg/var])))

(s/def :nrepl-msg/session clojure.core/string?)
(s/def :nrepl-msg/id clojure.core/string?)
;
(s/def :nrepl-msg/message clojure.core/string?)

(s/def :nrepl-msg/stacktrace-msg
  (s/keys :req-un [:nrepl-msg/session
                   :nrepl-msg/id
                   :nrepl-msg/class
                   :nrepl-msg/message
                   :nrepl-msg/stacktrace]))
