thheller 12:31 Uhr
hey, mir ist noch was eingefallen ...
wenn du eh eine server connection hast dann kannst du den ganzen self-hosted kram auch einfach weglassen

und stattdessen das "backend" von der shadow-cljs REPL verwenden
awb99 12:32 Uhr
ja
das ist auch noch ne moeglichkeit
thheller 12:32 Uhr
ist zwar nicht part von irgendeiner offiziellen API aber ist recht einfach
wenn du im shadow-cljs project einfach mal lein with-profiles +cljs repl startest
awb99 12:34 Uhr
Das werde ich zumindest einmal versuchebn
thheller 12:34 Uhr
und dann diesen test-ns laedst
https://github.com/thheller/shadow-cljs/blob/2f4d86b37b8f0c3417268faf373b0620051507fe/src/repl/shadow/cljs/repl_test.clj#L123
src/repl/shadow/cljs/repl_test.clj:123
(deftest test-repl-ns-with-js
<https://github.com/thheller/shadow-cljs|thheller/shadow-cljs>thheller/shadow-cljs | Hinzugefügt von GitHub
und dann zb (test-repl-ns-with-js)
awb99 12:34 Uhr
Wir wollen die notebooks auf pink-gorilla.org hosten
mit kubernetes cluster
Meiner Meinung nach das schwierigste an Clojure ist das build und development environment zum laufen zu kriegen
thheller 12:35 Uhr
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
awb99 12:36 Uhr
Wir haben schon alles laufen,
aber die session ids die die notebooks mit laufenden neuen kerneln linkt,
thheller 12:36 Uhr
jeder "process-input" call ist quasi ein notebook eintrag
awb99 12:36 Uhr
diese kubernetes middleware macht noch probleme.
SEHR COOL!
1000 DANK!!
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
awb99 12:38 Uhr
Sehr cool!
thheller 12:38 Uhr
:repl-sources sind die source-ids die der client geladen haben muss
die bekommst du direkt aus dem build state (oder von disk via "flush") (bearbeitet) 
awb99 12:40 Uhr
laedt er so dependencies auch nach?
thheller 12:40 Uhr
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
awb99 12:43 Uhr
Und weniger bundle size
absolut
Wir werden das notebook von figwheel main auf shadow-cljs migrieren
sobald das gemcht ist
nehmen wir einen cljs kernel mit shadow-direct auf
thheller 12:45 Uhr
du kannst dann deinen gesamten client side code getrennt laufen lassen und sogar per :advanced
awb99 12:45 Uhr
per advanced
wow
thheller 12:45 Uhr
und den notebook code einfach separat handhaben. evtl in nem web-worker oder so
awb99 12:45 Uhr
das ist ja geil
ich haett noch 2 mini fragen
kann ich shadow cljs libraries requiren ?
(bearbeitet)
(defproject  org.pinkgorilla/kernel-cljs-shadow "0.0.3"
  :description "A cñljs kernel using shadow-cljs for PinkGorilla Notebook."
  :url "https://github.com/pink-gorilla/kernel-cljs-shadow"
  :license {:name "MIT"}
  ;:deploy-repositories [["releases" :clojars]]
  :repositories [["clojars" {:url "https://clojars.org/repo"
                             :username "pinkgorillawb"
                             :sign-releases false}]]
  :dependencies
  [ ; dependencies of cljs-kernel-shadow. The gorilla-notebook will automatically fetch this transient dependencies
   [thheller/shadow-cljs "2.0.40"] ; needed for the deopendency loader in self hosted clojurescript
   [cljs-await "1.0.2"]  ; "promisify cllbacks"
   ]
  :source-paths ["src"])

pink-gorilla/kernel-cljs-shadow
Language
Clojure
Last updated
an hour ago
<https://github.com/pink-gorilla/kernel-cljs-shadow|pink-gorilla/kernel-cljs-shadow>pink-gorilla/kernel-cljs-shadow | 25. Nov. | Hinzugefügt von GitHub
thheller 12:47 Uhr
was sind shadow-cljs libraries?
awb99 12:47 Uhr
{:dependencies
 [[org.clojure/spec.alpha  "0.2.176"]
  [org.clojure/tools.reader "1.3.2"]
  [org.clojure/core.async  "0.4.474"]
  ;[thheller/shadow-cljs "2.0.40"] ; not needed as shadow-cljs is executing this file already
  [cljs-await "1.0.2"]  ; "promisify cllbacks"
  [appliedscience/js-interop  "0.1.13"] ; for js stuff - currently not needed
  [cljs-http   "0.1.42"]
  ; dependencies o demo-app in :demo build
  [re-view "0.4.6"]
  [lark/cells "0.1.5"]
  [lark/tools "0.1.19"]
  [maria/shapes "0.1.0"]
  [thi.ng/geom "0.0.908"]]
 :source-paths ["src"   ; cljs-kernel-shadow
                "test"  ; unit tests for cljs-kernel-test
                "env/dev"  ; demo app
                ]
 :nrepl        {:port 8703}
 :builds       {:demo     {:target           :browser
                           :output-dir       "out/public/js"
                           :asset-path       "/js"
                           :compiler-options {:optimizations :simple}
                           :modules          {:base {:entries [demo.core]}}
                           :devtools         {:http-root  "out/public"
                                              :http-port  8702
                                              :after-load demo.core/render}}
                :my-tests {:target :browser-test
                           :test-dir "out/demo-test-dummy"
                           :devtools
                           {:http-port 8606
                            :http-root "out/demo-test-dummy"}}}}
thheller 12:47 Uhr
please don't use shadow-cljs 2.0.40 ... thats like ancient history :herausgestreckte_zunge: (bearbeitet) 
awb99 12:47 Uhr
also ich hab ein projekt
hab project.clj
und shadow-cljs gepostet
im notebook
im project.clj
; cljs-kernel-shadow
                 [org.pinkgorilla/kernel-cljs-shadow "0.0.3"]
im notebook hol ich mir das rein
damit holt er sich die transient dependencies aus der project.clj des kernels
geht es auch das ganze nur ueber shadow cljs zu machen ?
thheller 12:50 Uhr
verstehe die frage nicht sorry
aber denk dran das shadow-cljs dafuer gemacht ist CLJS projekte zu managen (bearbeitet) 
nicht CLJ projekte
awb99 12:51 Uhr
mein cljs-kernel projekt ist ein reines cljs projekt
wenn ich jetzt mit dieses projekt im notebook laden will,
dann mach ich das im moment ueber clojure wieder.
thheller 12:52 Uhr
also so wie ich das bauen wuerde: du baust einen CLJ server der die CLJS compilation handhabnt
diese included shadow-cljs als library
das frontend .. wie auch immer es gebaut ist ist separat davon. regular CLJS :browser build.
das spricht dann mit dem server um den "notebook" kram zu machen
also dynamisch code compilen etc
und laedt diesen code separate (in einer iframe/web-worker zb)
Neue Nachrichten
ich fahre nochmal eben zum training ...
awb99 12:54 Uhr
du 1000 dank!
Mit Deiner Anleitung trau ich mich da drueber.
Ich wollte mit Figwheel Main schon  was aehnliches bauen,
aber da war zuviel undokumentierter code drin,
Wahrscheinlich waere es sogar am aller coolsten,
wenn dieser server gleich nrepl protokoll spricht.
Dann kann jede IDE
gleich dynamische skripts machen.
Ich werde ein test ballon starten
:leichtes_lächeln: