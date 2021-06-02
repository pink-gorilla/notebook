(ns demo.views.notebook
  (:require
   [re-frame.core :as rf]
   [pinkgorilla.notebook-ui.views.notebook :refer [notebook-component]]
  ))


(def snippets
  ["(+ 1 1)(println 1)\n {:a 5 :b \"ttt\"}"
   "(+ 2 2)(println 2)"
   "(+ 3 3)(println 3)"])


(defn codemirror-demo []
  (fn []
    (rf/dispatch [:document/load-snippets snippets])
    [notebook-component]
    ))

