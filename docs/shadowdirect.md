
im shadow-cljs project einfach mal lein with-profiles +cljs repl startest
und dann diesen test-ns laedst
https://github.com/thheller/shadow-cljs/blob/2f4d86b37b8f0c3417268faf373b0620051507fe/src/repl/shadow/cljs/repl_test.clj#L123
src/repl/shadow/cljs/repl_test.clj:123
(deftest test-repl-ns-with-js
<https://github.com/thheller/shadow-cljs|thheller/shadow-cljs>thheller/shadow-cljs | Hinzugefügt von GitHub
und dann zb (test-repl-ns-with-js)

oder dummes beispiel wie
(deftest test-repl-ns-with-js
  (let [{:keys [repl-state] :as state}
        (-> (basic-repl-setup)
            (api/with-js-options {:js-provider :shadow})
            (repl/process-input "(def x 1)")
            (repl/process-input "(inc x)")
            (repl/process-input "(def y 3)")
            (repl/process-input "(- x y)")
            )]
    (pprint repl-state)))

thheller 12:37 Uhr
{:shadow.cljs.repl/repl-state true,
 :current-ns cljs.user,
 :repl-sources
 [[:shadow.build.classpath/resource "goog/base.js"]
  [:shadow.build.classpath/resource "goog/debug/error.js"]
  [:shadow.build.classpath/resource "goog/dom/nodetype.js"]
  [:shadow.build.classpath/resource "goog/asserts/asserts.js"]
  [:shadow.build.classpath/resource "goog/reflect/reflect.js"]
  [:shadow.build.classpath/resource "goog/math/long.js"]
  [:shadow.build.classpath/resource "goog/math/integer.js"]
  [:shadow.build.classpath/resource "goog/dom/asserts.js"]
  [:shadow.build.classpath/resource "goog/functions/functions.js"]
  [:shadow.build.classpath/resource "goog/array/array.js"]
  [:shadow.build.classpath/resource "goog/dom/htmlelement.js"]
  [:shadow.build.classpath/resource "goog/dom/tagname.js"]
  [:shadow.build.classpath/resource "goog/object/object.js"]
  [:shadow.build.classpath/resource "goog/dom/tags.js"]
  [:shadow.build.classpath/resource "goog/html/trustedtypes.js"]
  [:shadow.build.classpath/resource "goog/string/typedstring.js"]
  [:shadow.build.classpath/resource "goog/string/const.js"]
  [:shadow.build.classpath/resource "goog/html/safescript.js"]
  [:shadow.build.classpath/resource "goog/fs/url.js"]
  [:shadow.build.classpath/resource "goog/i18n/bidi.js"]
  [:shadow.build.classpath/resource "goog/html/trustedresourceurl.js"]
  [:shadow.build.classpath/resource "goog/string/internal.js"]
  [:shadow.build.classpath/resource "goog/html/safeurl.js"]
  [:shadow.build.classpath/resource "goog/html/safestyle.js"]
  [:shadow.build.classpath/resource "goog/html/safestylesheet.js"]
  [:shadow.build.classpath/resource "goog/labs/useragent/util.js"]
  [:shadow.build.classpath/resource "goog/labs/useragent/browser.js"]
  [:shadow.build.classpath/resource "goog/html/safehtml.js"]
  [:shadow.build.classpath/resource
   "goog/html/uncheckedconversions.js"]
  [:shadow.build.classpath/resource "goog/dom/safe.js"]
  [:shadow.build.classpath/resource "goog/string/string.js"]
  [:shadow.build.classpath/resource "goog/structs/structs.js"]
  [:shadow.build.classpath/resource "goog/math/math.js"]
  [:shadow.build.classpath/resource "goog/iter/iter.js"]
  [:shadow.build.classpath/resource "goog/structs/map.js"]
  [:shadow.build.classpath/resource "goog/uri/utils.js"]
  [:shadow.build.classpath/resource "goog/uri/uri.js"]
  [:shadow.build.classpath/resource "goog/string/stringbuffer.js"]
  [:shadow.build.classpath/resource "cljs/core.cljs"]
  [:shadow.build.classpath/resource "clojure/walk.cljs"]
  [:shadow.build.classpath/resource "cljs/spec/gen/alpha.cljs"]
  [:shadow.build.classpath/resource "clojure/string.cljs"]
  [:shadow.build.classpath/resource "cljs/spec/alpha.cljs"]
  [:shadow.build.classpath/resource "goog/string/stringformat.js"]
  [:shadow.build.classpath/resource "cljs/repl.cljs"]
  [:shadow.cljs.repl/resource "cljs/user.cljs"]],
 :repl-actions
 [{:type :repl/invoke,
   :name "<eval>",
   :js
   "(function (){\r\ncljs.user.x = (1); return (\r\nnew cljs.core.Var(function(){return cljs.user.x;},new cljs.core.Symbol(\"cljs.user\",\"x\",\"cljs.user/x\",-156439873,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,\"ns\",\"ns\",441598760),new cljs.core.Keyword(null,\"name\",\"name\",1843675177),new cljs.core.Keyword(null,\"file\",\"file\",-1269645878),new cljs.core.Keyword(null,\"end-column\",\"end-column\",1425389514),new cljs.core.Keyword(null,\"source\",\"source\",-433931539),new cljs.core.Keyword(null,\"column\",\"column\",2078222095),new cljs.core.Keyword(null,\"line\",\"line\",212345235),new cljs.core.Keyword(null,\"end-line\",\"end-line\",1837326455),new cljs.core.Keyword(null,\"arglists\",\"arglists\",1661989754),new cljs.core.Keyword(null,\"doc\",\"doc\",1913296891),new cljs.core.Keyword(null,\"test\",\"test\",577538877)],[new cljs.core.Symbol(null,\"cljs.user\",\"cljs.user\",877795071,null),new cljs.core.Symbol(null,\"x\",\"x\",-555367584,null),\"cljs/user.cljs\",7,\"x\",1,1,1,cljs.core.List.EMPTY,null,(cljs.core.truth_(cljs.user.x)?cljs.user.x.cljs$lang$test:null)])));})()\r\n",
   :source "(def x 1)",
   :source-map-json
   "{\"version\":3,\r\n \"file\":\"<eval>\",\r\n \"sources\":[\"<eval>\"],\r\n \"lineCount\":1,\r\n \"mappings\":\r\n \"AAAA;AAAA,AAAKA;AAAL,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAAA,AAAA,AAAA,AAAAA,AAAA\",\r\n \"names\":[\"cljs.user/x\"],\r\n \"sourcesContent\":[\"(def x 1)\"]}\r\n",
   :warnings []}
  {:type :repl/invoke,
   :name "<eval>",
   :js "(cljs.user.x + (1))",
   :source "(inc x)",
   :source-map-json
   "{\"version\":3,\r\n \"file\":\"<eval>\",\r\n \"sources\":[\"<eval>\"],\r\n \"lineCount\":1,\r\n \"mappings\":\"AAAA,AAAA,AAAKA\",\r\n \"names\":[\"cljs.user/x\"],\r\n \"sourcesContent\":[\"(inc x)\"]}\r\n",
   :warnings []}
  {:type :repl/invoke,
   :name "<eval>",
   :js
   "(function (){\r\ncljs.user.y = (3); return (\r\nnew cljs.core.Var(function(){return cljs.user.y;},new cljs.core.Symbol(\"cljs.user\",\"y\",\"cljs.user/y\",558816894,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,\"ns\",\"ns\",441598760),new cljs.core.Keyword(null,\"name\",\"name\",1843675177),new cljs.core.Keyword(null,\"file\",\"file\",-1269645878),new cljs.core.Keyword(null,\"end-column\",\"end-column\",1425389514),new cljs.core.Keyword(null,\"source\",\"source\",-433931539),new cljs.core.Keyword(null,\"column\",\"column\",2078222095),new cljs.core.Keyword(null,\"line\",\"line\",212345235),new cljs.core.Keyword(null,\"end-line\",\"end-line\",1837326455),new cljs.core.Keyword(null,\"arglists\",\"arglists\",1661989754),new cljs.core.Keyword(null,\"doc\",\"doc\",1913296891),new cljs.core.Keyword(null,\"test\",\"test\",577538877)],[new cljs.core.Symbol(null,\"cljs.user\",\"cljs.user\",877795071,null),new cljs.core.Symbol(null,\"y\",\"y\",-117328249,null),\"cljs/user.cljs\",7,\"y\",1,1,1,cljs.core.List.EMPTY,null,(cljs.core.truth_(cljs.user.y)?cljs.user.y.cljs$lang$test:null)])));})()\r\n",
   :source "(def y 3)",
   :source-map-json
   "{\"version\":3,\r\n \"file\":\"<eval>\",\r\n \"sources\":[\"<eval>\"],\r\n \"lineCount\":1,\r\n \"mappings\":\r\n \"AAAA;AAAA,AAAKA;AAAL,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAA,AAAAA,AAAA,AAAA,AAAAA,AAAA\",\r\n \"names\":[\"cljs.user/y\"],\r\n \"sourcesContent\":[\"(def y 3)\"]}\r\n",
   :warnings []}
  {:type :repl/invoke,
   :name "<eval>",
   :js "(cljs.user.x - cljs.user.y)",
   :source "(- x y)",
   :source-map-json
   "{\"version\":3,\r\n \"file\":\"<eval>\",\r\n \"sources\":[\"<eval>\"],\r\n \"lineCount\":1,\r\n \"mappings\":\"AAAA,AAAGA,AAAEC\",\r\n \"names\":[\"cljs.user/x\", \"cljs.user/y\"],\r\n \"sourcesContent\":[\"(- x y)\"]}\r\n",
   :warnings []}]}
sieht nen bisl wahnsinn aus so (bearbeitet) 
aber das ist quasi die komplette server side von einer REPL
ohne das es selbst eval'd oder so
du wuerdest quasi die :repl-actions nehmen und zum client schicken und dort interpretieren
:repl/invoke machste quasi js/eval das :js (bearbeitet) 

:repl-sources sind die source-ids die der client geladen haben muss
die bekommst du direkt aus dem build state (oder von disk via "flush") (bearbeitet) 

aber der client ist kram ist recht minimal und du hast nichts von dem self-hosted kram client side
{:type :repl/require,
   :sources
   [[:shadow.build.classpath/resource "goog/base.js"]
    [:shadow.build.classpath/resource "goog/debug/error.js"]
    [:shadow.build.classpath/resource "goog/dom/nodetype.js"]
    [:shadow.build.classpath/resource "goog/asserts/asserts.js"]
    [:shadow.build.classpath/resource "goog/reflect/reflect.js"]
    [:shadow.build.classpath/resource "goog/math/long.js"]
    [:shadow.build.classpath/resource "goog/math/integer.js"]
    [:shadow.build.classpath/resource "goog/dom/asserts.js"]
    [:shadow.build.classpath/resource "goog/functions/functions.js"]
    [:shadow.build.classpath/resource "goog/array/array.js"]
    [:shadow.build.classpath/resource "goog/dom/htmlelement.js"]
    [:shadow.build.classpath/resource "goog/dom/tagname.js"]
    [:shadow.build.classpath/resource "goog/object/object.js"]
    [:shadow.build.classpath/resource "goog/dom/tags.js"]
    [:shadow.build.classpath/resource "goog/html/trustedtypes.js"]
    [:shadow.build.classpath/resource "goog/string/typedstring.js"]
    [:shadow.build.classpath/resource "goog/string/const.js"]
    [:shadow.build.classpath/resource "goog/html/safescript.js"]
    [:shadow.build.classpath/resource "goog/fs/url.js"]
    [:shadow.build.classpath/resource "goog/i18n/bidi.js"]
    [:shadow.build.classpath/resource
     "goog/html/trustedresourceurl.js"]
    [:shadow.build.classpath/resource "goog/string/internal.js"]
    [:shadow.build.classpath/resource "goog/html/safeurl.js"]
    [:shadow.build.classpath/resource "goog/html/safestyle.js"]
    [:shadow.build.classpath/resource "goog/html/safestylesheet.js"]
    [:shadow.build.classpath/resource "goog/labs/useragent/util.js"]
    [:shadow.build.classpath/resource "goog/labs/useragent/browser.js"]
    [:shadow.build.classpath/resource "goog/html/safehtml.js"]
    [:shadow.build.classpath/resource
     "goog/html/uncheckedconversions.js"]
    [:shadow.build.classpath/resource "goog/dom/safe.js"]
    [:shadow.build.classpath/resource "goog/string/string.js"]
    [:shadow.build.classpath/resource "goog/structs/structs.js"]
    [:shadow.build.classpath/resource "goog/math/math.js"]
    [:shadow.build.classpath/resource "goog/iter/iter.js"]
    [:shadow.build.classpath/resource "goog/structs/map.js"]
    [:shadow.build.classpath/resource "goog/uri/utils.js"]
    [:shadow.build.classpath/resource "goog/uri/uri.js"]
    [:shadow.build.classpath/resource "goog/string/stringbuffer.js"]
    [:shadow.build.classpath/resource "cljs/core.cljs"]
    [:shadow.build.classpath/resource "clojure/walk.cljs"]
    [:shadow.build.classpath/resource "cljs/spec/gen/alpha.cljs"]
    [:shadow.build.classpath/resource "clojure/string.cljs"]
    [:shadow.build.classpath/resource "cljs/spec/alpha.cljs"]
    [:shadow.build.classpath/resource "goog/string/stringformat.js"]
    [:shadow.build.classpath/resource "cljs/repl.cljs"]
    [:shadow.build.js-support/require "http"]
    [:shadow.build.js-support/require "request"]
    [:shadow.build.classpath/resource "shadow/js.js"]
    [:shadow.build.classpath/resource "demo/cjs.js"]
    [:shadow.build.js-support/require "which"]
    [:shadow.build.classpath/resource "demo/script.cljs"]],
   :warnings [],
   :reload-namespaces #{demo.script},
   :flags #{:reload}}]}
:repl/require ist die repl-action
quasi das war (require 'demo.script)
du kriegst die liste der :sources und der client entscheidet welche er schon hat
und requested den rest
wenn du eh eine server connection hast sollte das leichter sein als self-hosted

du kannst dann deinen gesamten client side code getrennt laufen lassen und sogar per :advanced
und den notebook code einfach separat handhaben. evtl in nem web-worker oder so



also so wie ich das bauen wuerde: du baust einen CLJ server der die CLJS compilation handhabnt
diese included shadow-cljs als library
das frontend .. wie auch immer es gebaut ist ist separat davon. regular CLJS :browser build.
das spricht dann mit dem server um den "notebook" kram zu machen
also dynamisch code compilen etc
und laedt diesen code separate (in einer iframe/web-worker zb)

