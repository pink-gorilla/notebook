(ns pinkgorilla.kernel.klipse-bundled-dependencies
 (:require 

  ; self hosted clojurescript
  cljs.analyzer.api
  cljs.analyzer
  cljs.env
  cljs.repl
  
  ; klipse 
  klipse-clj.repl
  klipse-clj.tools
  
  clojure.spec.alpha
  clojure.walk
  clojure.zip
  clojure.edn
  clojure.data
  clojure.datafy
  clojure.set
  
  ;; libraries we want in the bundle:
  reagent.core
  
  pinkgorilla.ui.gorilla-renderable
  pinkgorilla.ui.rendererCLJS
  pinkgorilla.kernel.cljs-tools
  
  fortune.core
  awb99.shapes.core
  quil.core
  quil.middleware

  ))
  
