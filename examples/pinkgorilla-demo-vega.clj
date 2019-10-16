;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla Demo - VEGA rendering
;;; This demo explores how VEGA charts can be rendered in pinkgorilla
;;; 
;;; Stolen from: https://github.com/metasoarous/oz/blob/master/src/clj/oz/notebook/clojupyter.clj
;; **

;; @@
(ns demo-vega
  (:require [pinkgorilla.ui.vega :refer [vega2!]]))
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(def sample-chart 
  {:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :data {:values [{:a "A" :b 28} 
                   {:a "B" :b 55} 
                   {:a "C" :b 43}
                   {:a "D" :b 91} 
                   {:a "E" :b 81} 
                   {:a "F" :b 53}
                   {:a "G" :b 19} 
                   {:a "H" :b 87} 
                   {:a "I" :b 52} 
                   {:a "J" :b 127}]}
   :mark "bar" 
   :encoding 
   {:x {:field "a" :type "ordinal"}
    :y {:field "b" :type "quantitative"}}})
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-var"],"#'demo-vega/sample-chart"],"~:value","#'demo-vega/sample-chart"]
;; <=

;; @@
(vega2! sample-chart)
;; @@
;; =>
;;; ["^ ","~:type","html","~:content","\n<div>\n  <script data-main='scripts/main' src='https://cdnjs.cloudflare.com/ajax/libs/require.js/2.3.6/require.min.js'></script>\n  <div id='uuid-195b6f21-c3a5-439d-a26b-44b434f853e7'> </div>\n  <script>\n     requirejs.config({\n       baseUrl: 'https://cdn.jsdelivr.net/npm/',\n       paths: {\n         'vega-embed':  'vega-embed@3?noext',\n         'vega-lib': 'vega-lib?noext',\n         'vega-lite': 'vega-lite@2?noext',\n         'vega': 'vega@3?noext'\n       }\n     });\n     require(['vega-embed'], function(vegaEmbed) {\n        let spec = {\"$schema\":\"https:\\/\\/vega.github.io\\/schema\\/vega-lite\\/v4.json\",\"description\":\"A simple bar chart with embedded data.\",\"data\":{\"values\":[{\"a\":\"A\",\"b\":28},{\"a\":\"B\",\"b\":55},{\"a\":\"C\",\"b\":43},{\"a\":\"D\",\"b\":91},{\"a\":\"E\",\"b\":81},{\"a\":\"F\",\"b\":53},{\"a\":\"G\",\"b\":19},{\"a\":\"H\",\"b\":87},{\"a\":\"I\",\"b\":52},{\"a\":\"J\",\"b\":127}]},\"mark\":\"bar\",\"encoding\":{\"x\":{\"field\":\"a\",\"type\":\"ordinal\"},\"y\":{\"field\":\"b\",\"type\":\"quantitative\"}}};\n        (console.log 'rendering vega spec: ' spec);\n        vegaEmbed('#uuid-195b6f21-c3a5-439d-a26b-44b434f853e7', spec, {defaultStyle:true}).catch(console.warn);\n    }, function(err) {\n        console.log('Failed to load');\n    });\n  </script>\n</div>\n  "]
;; <=

;; @@

;; @@
;; ->
;;; 
;; <-
