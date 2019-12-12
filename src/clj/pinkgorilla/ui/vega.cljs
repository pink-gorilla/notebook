(ns pinkgorilla.ui.vega
  "reagent component that render vega-charts"
  (:require
   [pinkgorilla.output.jsscript :refer [output-jsscript]]))

;; copied from renderable
;; this bypasses the module system - needed because cljs has to be compiled

;; it however <DOES use ou js dependency management system

(def module "
  define([], function () {
      return {
         version: 'vega 0.0.4',
         render: function (id_or_domel, data) {
            var selector_or_domel = id_or_domel;
            if (typeof id_or_domel === 'string' || id_or_domel instanceof String) {
               selector_or_domel = '#'+ id_or_domel;
               console.log ('vega-module is rendering to selector id: ' + selector_or_domel);
            } else {
               console.log ('vega-module is rendering to dom-element');
            }
            var dataJson = JSON.stringify(data)
            console.log ('vega-module data: ' + dataJson);
            require(['vega', 'vega-lite', 'vega-embed'], function(vega, vegaLite, vegaEmbed) {
              vegaEmbed(selector_or_domel, data, {defaultStyle:true}).catch(function(em) {
                  console.log('Error in Rendering Vega Spec: ' + em)
                 });
              }, function(err) {
                console.log('Vega-Embed failed to load');
            });
         }
      }
  });
")

(defn vega
  [spec]
  [output-jsscript {:content {:module module :data spec}}])

(defn vegaa [a k]
  (let [spec (k @a)]
    (if (nil? spec)
      [:p "spec missing"]
      [:div
           ; [:p  (k @a)]
       [output-jsscript {:content {:module module :data spec}}]])))



