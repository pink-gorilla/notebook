(ns pinkgorilla.notebook-ui.codemirror.extension.parinfer
  (:require
   [reagent.core :as r]))

(defn on-cm-init [inst]
  #_(parinfer/parinferize! inst (swap! pi-count inc)
                           :indent-mode (.getValue inst))
  (println "parinferize!"))