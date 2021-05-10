(ns user2)
(require '[spec-provider.provider :as sp])
(require '[pinkgorilla.notebook-ui.schema :as x])
(require '[pinkgorilla.notebook-ui.schema-sample :as d])
(require '[clojure.spec.alpha :as s])

; https://github.com/stathissideris/spec-provider


(def ddd d/keybindings)
;; => #'user2/ddd

(println "data" ddd)

(def inferred-specs
  (sp/infer-specs
   ;s/settings
   ;s/document
   ddd
   :gorilla/db
   ))

;(clojure.pprint/pprint db)
(sp/pprint-specs inferred-specs 'gorilla 's)


(s/valid? :pinkgorilla.notebook-ui.schema/db d/db)
        