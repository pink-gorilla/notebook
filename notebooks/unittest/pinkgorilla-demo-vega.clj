;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla Demo - VEGA rendering
;;; This demo explores how VEGA charts can be rendered in pinkgorilla
;;; 
;;; Stolen from: https://github.com/metasoarous/oz/blob/master/src/clj/oz/notebook/clojupyter.clj
;; **

;; @@
(ns demo-vega
  (:require [pinkgorilla.ui.vega :refer [vega! module]]))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(def sample-chart 
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :mode "vega-lite"
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
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-var"],"#'demo-vega/sample-chart"],"~:value","#'demo-vega/sample-chart"]
;; <=

;; @@
(vega! sample-chart)
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data",["^ ","~:mode","vega-lite","~:description","A simple bar chart with embedded data.","^2",["^ ","~:values",[["^ ","~:a","A","~:b",200],["^ ","~:a","B","~:b",55],["^ ","~:a","C","~:b",43],["^ ","~:a","D","~:b",91],["^ ","~:a","E","~:b",81],["^ ","~:a","F","~:b",53],["^ ","~:a","G","~:b",19],["^ ","~:a","H","~:b",87],["^ ","~:a","I","~:b",52],["^ ","~:a","J","~:b",127]]],"~:mark","bar","~:encoding",["^ ","~:x",["^ ","~:field","a","^0","ordinal"],"~:y",["^ ","^8","b","^0","quantitative"]]],"~:module","\n  define([], function () {\n      return {\n         version: 'vega 0.0.4',\n         render: function (id_or_domel, data) {\n            var selector_or_domel = id_or_domel;\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\n               selector_or_domel = '#'+ id_or_domel;\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\n            } else {\n               console.log ('vega-module is rendering to dom-element');\n            }\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module data: ' + dataJson);\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\n                  console.log('Error in Rendering Vega Spec: ' + em)\n                 });\n              }, function(err) {\n                console.log('Vega-Embed failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@
; Load Vega directly from a vega specification in an URL
(vega! "https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json")  
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data","https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json","~:module","\n  define([], function () {\n      return {\n         version: 'vega 0.0.4',\n         render: function (id_or_domel, data) {\n            var selector_or_domel = id_or_domel;\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\n               selector_or_domel = '#'+ id_or_domel;\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\n            } else {\n               console.log ('vega-module is rendering to dom-element');\n            }\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module data: ' + dataJson);\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\n                  console.log('Error in Rendering Vega Spec: ' + em)\n                 });\n              }, function(err) {\n                console.log('Vega-Embed failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@
; show the code of the vega module (useful for debugging.)
module
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-string"],"\"\\n  define([], function () {\\n      return {\\n         version: 'vega 0.0.4',\\n         render: function (id_or_domel, data) {\\n            var selector_or_domel = id_or_domel;\\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\\n               selector_or_domel = '#'+ id_or_domel;\\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\\n            } else {\\n               console.log ('vega-module is rendering to dom-element');\\n            }\\n            var dataJson = JSON.stringify(data)\\n            console.log ('vega-module data: ' + dataJson);\\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\\n                  console.log('Error in Rendering Vega Spec: ' + em)\\n                 });\\n              }, function(err) {\\n                console.log('Vega-Embed failed to load');\\n            });\\n         }\\n      }\\n  });\\n\""],"~:value","\"\\n  define([], function () {\\n      return {\\n         version: 'vega 0.0.4',\\n         render: function (id_or_domel, data) {\\n            var selector_or_domel = id_or_domel;\\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\\n               selector_or_domel = '#'+ id_or_domel;\\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\\n            } else {\\n               console.log ('vega-module is rendering to dom-element');\\n            }\\n            var dataJson = JSON.stringify(data)\\n            console.log ('vega-module data: ' + dataJson);\\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\\n                  console.log('Error in Rendering Vega Spec: ' + em)\\n                 });\\n              }, function(err) {\\n                console.log('Vega-Embed failed to load');\\n            });\\n         }\\n      }\\n  });\\n\""]
;; <=

;; @@
(vega! {:a 3 :b 4})
;; @@
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data",["^ ","~:a",3,"~:b",4],"~:module","\n  define([], function () {\n      return {\n         version: 'vega 0.0.4',\n         render: function (id_or_domel, data) {\n            var selector_or_domel = id_or_domel;\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\n               selector_or_domel = '#'+ id_or_domel;\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\n            } else {\n               console.log ('vega-module is rendering to dom-element');\n            }\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module data: ' + dataJson);\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\n                  console.log('Error in Rendering Vega Spec: ' + em)\n                 });\n              }, function(err) {\n                console.log('Vega-Embed failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@

;; @@
