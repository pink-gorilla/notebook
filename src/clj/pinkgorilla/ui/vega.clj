(ns pinkgorilla.ui.vega
  "plugin to render vega-charts in pink-gorilla
   (TODO: move to own library)
"
  (:require 
   ;[org.clojars.deas/gorilla-plot]
   [gorilla-renderable.core :refer :all]
   [hiccup.core :as hiccup]
   [clojure.data.json :as json]
   ;[hiccup.page :refer [html5 include-css include-js]]
   ; TODO: use include-js instead of pure javascript code below.
   ))


(def require-string
  "
<div>
  <script data-main='scripts/main' src='https://cdnjs.cloudflare.com/ajax/libs/require.js/2.3.6/require.min.js'></script>
  <div id='uuid-%s'> </div>
  <script>
     console.log('bongo.')
     requirejs.config({
       baseUrl: 'https://cdn.jsdelivr.net/npm/',
       paths: {
         'vega-embed':  'vega-embed@3?noext',
         'vega-lib': 'vega-lib?noext',
         'vega-lite': 'vega-lite@2?noext',
         'vega': 'vega@3?noext'
       }
     });
     require(['vega-embed'], function(vegaEmbed) {
        let spec = %s;
        (console.log 'rendering vega spec: ' spec);
        vegaEmbed('#uuid-%s', spec, {defaultStyle:true}).catch(console.warn);
    }, function(err) {
        console.log('Failed to load');
    });
  </script>
</div>
  ")

(defn- uuid [] (str (java.util.UUID/randomUUID)))

(defn- live-embed [v]
  (let [id (uuid)]
    (format require-string id (json/write-str v) id)))



(defn ^:no-doc embed
  "Take hiccup or vega/lite spec and embed the vega/lite portions using vegaEmbed, as hiccup :div and :script blocks.
  When rendered, should present as live html page; Currently semi-private, may be made fully public in future."
  ([spec {:as opts :keys [embed-fn] :or {embed-fn live-embed}}]
   ;; prewalk spec, rendering special hiccup tags like :vega and :vega-lite, and potentially other composites,
   ;; rendering using the components above. Leave regular hiccup unchanged).
   ;; TODO finish writing; already hooked in below so will break now
   (if (map? spec)
     (embed-fn spec)
     (clojure.walk/prewalk
      (fn [x] (if (and (coll? x) (#{:vega :vega-lite} (first x)))
                (embed-fn (second x))
                x))
      spec)))
  ([spec]
   (embed spec {})))


(defrecord Vega2 [specs])
(extend-type Vega2
  Renderable
  (render [self]
   {:type :html
    :content (embed (:specs self))
    ;:value (pr-str self)
    }
   ))

(defn vega2! [spec]
  "renders vega/vega-lite specification to a gorilla cell
   syntactical sugar only
   easier to use than to use (Vega. spec)"
  (Vega2. spec))




