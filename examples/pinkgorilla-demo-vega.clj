;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla Demo - VEGA rendering
;;; This demo explores how VEGA charts can be rendered in pinkgorilla
;;; 
;;; Stolen from: https://github.com/metasoarous/oz/blob/master/src/clj/oz/notebook/clojupyter.clj
;; **

;; @@
(ns demo-vega
  (:require [pinkgorilla.ui.vega :refer [vega!]]))
;; @@
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(def sample-chart 
  {:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :data {:values [{:a "A" :b 200} 
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
(vega! sample-chart)
;; @@
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data",["^ ","~:$schema","https://vega.github.io/schema/vega-lite/v4.json","~:description","A simple bar chart with embedded data.","^2",["^ ","~:values",[["^ ","~:a","A","~:b",200],["^ ","~:a","B","~:b",55],["^ ","~:a","C","~:b",43],["^ ","~:a","D","~:b",91],["^ ","~:a","E","~:b",81],["^ ","~:a","F","~:b",53],["^ ","~:a","G","~:b",19],["^ ","~:a","H","~:b",87],["^ ","~:a","I","~:b",52],["^ ","~:a","J","~:b",127]]],"~:mark","bar","~:encoding",["^ ","~:x",["^ ","~:field","a","^0","ordinal"],"~:y",["^ ","^8","b","^0","quantitative"]]],"~:module","\n  define([], function () {\n      return {\n         render: function (id, data) {\n            console.log ('vega-module is rendering to id: ' + id);\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module-test data: ' + dataJson);\n            require(['vega-embed'], function(vegaEmbed) {\n              let spec = data;\n              let id_selector = '#' + id;\n              vegaEmbed(id_selector, spec, {defaultStyle:true}).catch(console.warn);\n              }, function(err) {\n                console.log('Failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@
(vega! sample-chart)
;; @@
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data",["^ ","~:$schema","https://vega.github.io/schema/vega-lite/v4.json","~:description","A simple bar chart with embedded data.","^2",["^ ","~:values",[["^ ","~:a","A","~:b",200],["^ ","~:a","B","~:b",55],["^ ","~:a","C","~:b",43],["^ ","~:a","D","~:b",91],["^ ","~:a","E","~:b",81],["^ ","~:a","F","~:b",53],["^ ","~:a","G","~:b",19],["^ ","~:a","H","~:b",87],["^ ","~:a","I","~:b",52],["^ ","~:a","J","~:b",127]]],"~:mark","bar","~:encoding",["^ ","~:x",["^ ","~:field","a","^0","ordinal"],"~:y",["^ ","^8","b","^0","quantitative"]]],"~:module","\n  define([], function () {\n      return {\n         render: function (id, data) {\n            console.log ('vega-module is rendering to id: ' + id);\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module-test data: ' + dataJson);\n            require(['vega-embed'], function(vegaEmbed) {\n              let spec = data;\n              let id_selector = '#' + id;\n              vegaEmbed(id_selector, spec, {defaultStyle:true}).catch(console.warn);\n              }, function(err) {\n                console.log('Failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@

;; @@
