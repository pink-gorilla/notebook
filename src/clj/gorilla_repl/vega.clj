(ns gorilla-repl.vega
  (:require [pinkgorilla.ui.vega :as vega]))

;; TODO: Fix me for compatibility?


#_(def vega-view vega/vega-view)

#_(extend-type VegaView
    render/Renderable
    (render [self]
      {:type :vega :content (:content self) :value (pr-str self)}))
