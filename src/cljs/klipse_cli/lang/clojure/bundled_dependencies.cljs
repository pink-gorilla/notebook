(ns klipse-cli.lang.clojure.bundled-dependencies
;pinkgorilla.kernel.bundled-dependencies
 (:require 
  cljs.analyzer.api
  cljs.analyzer
  cljs.env
  cljs.repl
  klipse-clj.repl
  clojure.spec.alpha
  clojure.walk
  clojure.zip
  clojure.edn
  clojure.data
  clojure.datafy
  clojure.set
  klipse-clj.tools
  
  ;; libraries we want in the bundle:
  fortune.core
  ))
  
