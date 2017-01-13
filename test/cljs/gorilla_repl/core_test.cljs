(ns gorilla-repl.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [reagent.core :as reagent :refer [atom]]
            [gorilla-repl.views :as gv]))


(def isClient (not (nil? (try (.-document js/window)
                              (catch js/Object e nil)))))

(def rflush reagent/flush)

(defn add-test-div [name]
  (let [doc     js/document
        body    (.-body js/document)
        div     (.createElement doc "div")]
    (.appendChild body div)
    div))

(defn with-mounted-component [comp f]
  (when isClient
    (let [div (add-test-div "_testreagent")]
      (let [comp (reagent/render-component comp div #(f comp div))]
        (reagent/unmount-component-at-node div)
        (reagent/flush)
        (.removeChild (.-body js/document) div)))))


(defn found-in [re div]
  (let [res (.-innerHTML div)]
    (if (re-find re res)
      true
      (do (println "Not found: " res)
          false))))


(deftest test-component
  (with-mounted-component (gv/hamburger)
    (fn [c div]
      (is div)
      ;; (is (found-in #"Welcome to" div))
      )))


(deftest codemirror
  (let [cm (.-CodeMirror js/window)]
    (is cm)
    (is (.. cm -prototype -findMatchingBracket))
    ;; (is (.. cm -modes -clojure-parinfer)) ;; nil?
    (is (.. cm -modes -clojure))
    (is (.. cm -modes -markdown))
    (is (.-colorize cm))
    (is (.-runMode cm))
    (is (.-showHint cm))))

;; [cljsjs.codemirror.addon.edit.closebrackets]
;;  [cljsjs.codemirror.addon.edit.matchbrackets]
;; [cljsjs.codemirror.addon.runmode.runmode]
;; [cljsjs.codemirror.addon.runmode.colorize]
;; [cljsjs.codemirror.addon.hint.show-hint]
;; [cljsjs.codemirror.mode.clojure]
;; [cljsjs.codemirror.mode.clojure-parinfer]
;; [cljsjs.codemirror.mode.markdown]
;; [cljsjs.codemirror.mode.xml]
;; CodeMirror.prototype.findMatchingBracket
;; CodeMirror.modes['clojure-parinfer']
;; CodeMirror.modes['clojure']
;; CodeMirror.modes['markdown']
;; CodeMirror.colorize
;; CodeMirror.runMode
;; CodeMirror.showHint
