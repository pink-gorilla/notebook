;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla 
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands.
;;; 
;;; It's a good habit to run each worksheet in its own namespace. We created a random namespace for you; you can keep using it.
;; **

;; @@
; Automatically Download Dependencies (if they are not installed already) 
 (use '[pinkgorilla.helper]) 
 (pinkgorilla.helper/add-dependencies '[pinkgorilla.ui.gorilla-plot "0.8.6"])
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","list-like","~:open",["span",["^ ","~:class","clj-map"],"{"],"~:close",["span",["^ ","^2","clj-map"],"}"],"~:separator",["span",", "],"~:items",[["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","~:content",["span",["^ ","^2","clj-symbol"],"pinkgorilla.ui.gorilla-plot"],"~:value","pinkgorilla.ui.gorilla-plot"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"0.8.6\""],"^7","\"0.8.6\""]],"^7","[pinkgorilla.ui.gorilla-plot \"0.8.6\"]"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-set"],"#{"],"^3",["span",["^ ","^2","clj-set"],"}"],"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"pinkgorilla.ui.gorilla-renderable"],"^7","pinkgorilla.ui.gorilla-renderable"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"2.0.10\""],"^7","\"2.0.10\""]],"^7","[pinkgorilla.ui.gorilla-renderable \"2.0.10\"]"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/clojure"],"^7","org.clojure/clojure"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.9.0-alpha14\""],"^7","\"1.9.0-alpha14\""]],"^7","[org.clojure/clojure \"1.9.0-alpha14\"]"]],"^7","#{[pinkgorilla.ui.gorilla-renderable \"2.0.10\"] [org.clojure/clojure \"1.9.0-alpha14\"]}"]],"^7","[[pinkgorilla.ui.gorilla-plot \"0.8.6\"] #{[pinkgorilla.ui.gorilla-renderable \"2.0.10\"] [org.clojure/clojure \"1.9.0-alpha14\"]}]"],["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/clojure"],"^7","org.clojure/clojure"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.9.0-alpha14\""],"^7","\"1.9.0-alpha14\""]],"^7","[org.clojure/clojure \"1.9.0-alpha14\"]"],["^ ","^0","html","^6",["span",["^ ","^2","clj-nil"],"nil"],"^7","nil"]],"^7","[[org.clojure/clojure \"1.9.0-alpha14\"] nil]"],["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"pinkgorilla.ui.gorilla-renderable"],"^7","pinkgorilla.ui.gorilla-renderable"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"2.0.10\""],"^7","\"2.0.10\""]],"^7","[pinkgorilla.ui.gorilla-renderable \"2.0.10\"]"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-set"],"#{"],"^3",["span",["^ ","^2","clj-set"],"}"],"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"hiccup"],"^7","hiccup"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.0.5\""],"^7","\"1.0.5\""]],"^7","[hiccup \"1.0.5\"]"],["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/data.json"],"^7","org.clojure/data.json"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"0.2.6\""],"^7","\"0.2.6\""]],"^7","[org.clojure/data.json \"0.2.6\"]"]],"^7","#{[hiccup \"1.0.5\"] [org.clojure/data.json \"0.2.6\"]}"]],"^7","[[pinkgorilla.ui.gorilla-renderable \"2.0.10\"] #{[hiccup \"1.0.5\"] [org.clojure/data.json \"0.2.6\"]}]"],["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"org.clojure/data.json"],"^7","org.clojure/data.json"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"0.2.6\""],"^7","\"0.2.6\""]],"^7","[org.clojure/data.json \"0.2.6\"]"],["^ ","^0","html","^6",["span",["^ ","^2","clj-nil"],"nil"],"^7","nil"]],"^7","[[org.clojure/data.json \"0.2.6\"] nil]"],["^ ","^0","list-like","^1",null,"^3",null,"^4",["span"," "],"^5",[["^ ","^0","list-like","^1",["span",["^ ","^2","clj-vector"],"["],"^3",["span",["^ ","^2","clj-vector"],"]"],"^4",["span"," "],"^5",[["^ ","^0","html","^6",["span",["^ ","^2","clj-symbol"],"hiccup"],"^7","hiccup"],["^ ","^0","html","^6",["span",["^ ","^2","clj-string"],"\"1.0.5\""],"^7","\"1.0.5\""]],"^7","[hiccup \"1.0.5\"]"],["^ ","^0","html","^6",["span",["^ ","^2","clj-nil"],"nil"],"^7","nil"]],"^7","[[hiccup \"1.0.5\"] nil]"]],"^7","{[pinkgorilla.ui.gorilla-plot \"0.8.6\"] #{[pinkgorilla.ui.gorilla-renderable \"2.0.10\"] [org.clojure/clojure \"1.9.0-alpha14\"]}, [org.clojure/clojure \"1.9.0-alpha14\"] nil, [pinkgorilla.ui.gorilla-renderable \"2.0.10\"] #{[hiccup \"1.0.5\"] [org.clojure/data.json \"0.2.6\"]}, [org.clojure/data.json \"0.2.6\"] nil, [hiccup \"1.0.5\"] nil}"]
;; <=

;; @@
; Define Namespace for your notebook and require namespaces 
(ns pacific-pond  
  (:require 
     [pinkgorilla.ui.hiccup :refer [html!]] 
     [pinkgorilla.ui.vega :refer [vega!]] 
     [pinkgorilla.ui.reagent :refer [reagent!]] 
     [pinkgorilla.ui.gorilla-plot.core :refer [list-plot bar-chart compose histogram plot]]))

;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
(html! 
  [:div 
    [:h4 "Hiccup Markup"] 
    [:div {:style "color:green;font-weight:bold;background-color:pink"} "World!"] 
    [:ol 
       [:li "The Pinkie"] 
       [:li "The Pinkie and the Brain"]  
       [:li "What will we be doing today?"]]  
    [:img {:height 100 :width 100 :src "https://images-na.ssl-images-amazon.com/images/I/61LeuO%2Bj0xL._SL1500_.jpg"}]])
            
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content","<div><h4>Hiccup Markup</h4><div style=\"color:green;font-weight:bold;background-color:pink\">World!</div><ol><li>The Pinkie</li><li>The Pinkie and the Brain</li><li>What will we be doing today?</li></ol><img height=\"100\" src=\"https://images-na.ssl-images-amazon.com/images/I/61LeuO%2Bj0xL._SL1500_.jpg\" width=\"100\" /></div>"]
;; <=

;; @@
 (list-plot [5 6 7 3 9 20] ) 

;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data",["^ ","~:$schema","https://vega.github.io/schema/vega/v3.0.json","~:width",400,"~:height",247.2188,"~:padding",["^ ","~:top",10,"~:left",55,"~:bottom",40,"~:right",10],"^2",[["^ ","~:name","76d5a4c7-d6bd-45e8-9bd6-7f2b4f66db88","~:values",[["^ ","~:x",0,"~:y",5],["^ ","~:x",1,"~:y",6],["^ ","~:x",2,"~:y",7],["^ ","~:x",3,"~:y",3],["^ ","~:x",4,"~:y",9],["^ ","~:x",5,"~:y",20]]]],"~:marks",[["^ ","^0","symbol","~:from",["^ ","^2","76d5a4c7-d6bd-45e8-9bd6-7f2b4f66db88"],"~:encode",["^ ","~:enter",["^ ","~:x",["^ ","~:scale","x","~:field","x"],"~:y",["^ ","^A","y","^B","y"],"~:fill",["^ ","~:value","steelblue"],"~:fillOpacity",["^ ","^D",1]],"~:update",["^ ","~:shape","circle","~:size",["^ ","^D",70],"~:stroke",["^ ","^D","transparent"]],"~:hover",["^ ","^H",["^ ","^D",210],"^I",["^ ","^D","white"]]]]],"~:scales",[["^ ","^;","x","^0","linear","~:range","width","~:zero",false,"~:domain",["^ ","^2","76d5a4c7-d6bd-45e8-9bd6-7f2b4f66db88","^B","x"]],["^ ","^;","y","^0","linear","^L","height","~:nice",true,"^M",false,"^N",["^ ","^2","76d5a4c7-d6bd-45e8-9bd6-7f2b4f66db88","^B","y"]]],"~:axes",[["^ ","~:orient","bottom","^A","x"],["^ ","^Q","left","^A","y"]]],"~:module","\n  define([], function () {\n      return {\n         version: 'vega 0.0.4',\n         render: function (id_or_domel, data) {\n            var selector_or_domel = id_or_domel;\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\n               selector_or_domel = '#'+ id_or_domel;\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\n            } else {\n               console.log ('vega-module is rendering to dom-element');\n            }\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module data: ' + dataJson);\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\n                  console.log('Error in Rendering Vega Spec: ' + em)\n                 });\n              }, function(err) {\n                console.log('Vega-Embed failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@
 (vega! "https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json") 

;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","jsscript","~:content",["^ ","~:data","https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json","~:module","\n  define([], function () {\n      return {\n         version: 'vega 0.0.4',\n         render: function (id_or_domel, data) {\n            var selector_or_domel = id_or_domel;\n            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {\n               selector_or_domel = '#'+ id_or_domel;\n               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);\n            } else {\n               console.log ('vega-module is rendering to dom-element');\n            }\n            var dataJson = JSON.stringify(data)\n            console.log ('vega-module data: ' + dataJson);\n            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {\n              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {\n                  console.log('Error in Rendering Vega Spec: ' + em)\n                 });\n              }, function(err) {\n                console.log('Vega-Embed failed to load');\n            });\n         }\n      }\n  });\n"]]
;; <=

;; @@
(reagent! 
  [:h1 "hellow"])
   
  
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","reagent","~:content",["h1","hellow"],"~:value","[:h1 \"hellow\"]"]
;; <=

;; @@
(reagent! 
  [:div 
   [:h1 "hello"]
   [:p "world"]])
   
   
   
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","reagent","~:content",["div",["h1","hello"],["p","world"]],"~:value","[:div [:h1 \"hello\"] [:p \"world\"]]"]
;; <=

;; @@
(reagent! 
  '[:div 
    [:h1 "hello"]
    [:p "world"]
    [widget.clock/binary-clock] 
    [widget.hello/world "abc"] 
    [bongo "asdf"]])
     
   
   
   
   
;; @@
;; =>
;;; ["^ ","~:type","reagent","~:content",["div",["h1","hello"],["p","world"],["binary-clock"],["world","abc"],["bongo","asdf"]],"~:value","[:div [:h1 \"hello\"] [:p \"world\"] [widget.clock/binary-clock] [widget.hello/world \"abc\"] [bongo \"asdf\"]]"]
;; <=

;; @@
(reagent! 
  '[:div 
    [widget.hello/world 5]]) 
    
;; @@
;; =>
;;; ["^ ","~:type","reagent","~:content",["div",["world",5]],"~:value","[:div [widget.hello/world 5]]"]
;; <=

;; @@

;; @@
