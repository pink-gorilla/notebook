(defproject org.clojars.deas/gorilla-repl-ng "0.3.6-SNAPSHOT"
  :description "A rich REPL for Clojure in the notebook style."
  :url "https://github.com/deas/gorilla-repl"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 ;;  ring-json introduces jackson along with its tail
                 ;; [ring/ring-json "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.2.0"]
                 [cider/cider-nrepl "0.14.0"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 ;; [cljs-tooling "0.2.0"]
                 [org.clojars.deas/gorilla-middleware "0.1.2"]
                 [org.clojars.deas/gorilla-plot "0.2.0"]
                 [grimradical/clj-semver "0.3.0" :exclusions [org.clojure/clojure]]
                 [org.slf4j/slf4j-api "1.7.16"]
                 [ch.qos.logback/logback-core "1.1.5"]
                 [ch.qos.logback/logback-classic "1.1.5"]
                 [com.taoensso/timbre "4.7.4"]
                 ;; Things get very noise with slf4j-timbre - needs configuration
                 ;; [com.fzakaria/slf4j-timbre "0.3.2"]
                 [cljs-ajax "0.5.8"]
                 ;; goog.dom should be enough
                 ;; [enfocus "2.1.1"]
                 [prismatic/dommy "1.1.0"]
                 ;; [selmer "1.0.3"] ;; django like templates - deps worth the hassle?
                 [reagent "0.6.0"
                  :exclusions [org.clojure/tools.reader
                               cljsjs/react]]
                 [re-com "0.9.0"]
                 [day8.re-frame/http-fx "0.1.2"]
                 [day8.re-frame/undo "0.3.2"]
                 [cljsjs/react "15.3.1-0"]
                 ;; [flupot "0.4.0"]
                 ;; [reagent-forms "0.5.27"]
                 ;; [reagent-utils "0.2.0"]
                 ;; [hickory "0.6.0"] html -> hiccup as very last ressort only
                 [replumb "0.2.4"]
                 [org.webjars/MathJax "2.6.1"]              ;; TODO Not quite sure about value
                 [re-frame "0.8.0"]
                 [com.cemerick/url "0.1.1"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [org.clojure/tools.cli "0.3.5"]
                 [ring "1.5.0"
                  :exclusions [ring/ring-jetty-adapter]]
                 [ring-cors "0.1.8"]
                 [ring/ring-defaults "0.2.1"
                  :exclusions [javax.servlet/servlet-api]]
                 ;;  [ring.middleware.logger "0.5.0"]
                 ;; [ring-webjars "0.1.1"]                     ;; Although not matching servlet3 paths
                 [ring-middleware-format "0.7.0"]
                 [javax.websocket/javax.websocket-api "1.0"]
                 [javax.servlet/javax.servlet-api "3.1.0"]
                 [compojure "1.5.1"]
                 [hiccup "1.0.5"]
                 [environ "1.1.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [org.clojure/clojurescript "1.9.293"
                  :scope "provided"]
                 [secretary "1.2.3"]
                 [cljsjs/parinfer "1.8.1-0"]
                 [cljsjs/codemirror "5.21.0-0"]
                 [cljsjs/vega "2.6.0-0"]
                 [cljsjs/d3geo "0.2.15-2"]
                 [cljsjs/d3 "3.5.16-0"]
                 [cljsjs/mousetrap "1.5.3-0"]
                 [cljsjs/marked "0.3.5-0"]
                 [com.taoensso/sente "1.11.0"]
                 [org.danielsz/system "0.3.1"]
                 [jarohen/chord "0.7.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [de.otto/tesla-microservice "0.6.0"]
                 [de.otto/tesla-httpkit "1.0.1"]
                 [clojail "1.0.6"]
                 [com.cemerick/piggieback "0.2.1"]
                 [com.cemerick/pomegranate "0.3.1"]]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.4"]
            [lein-ring "0.9.7"]
            [lein-asset-minifier "0.3.0"
             :exclusions [org.clojure/clojure]]]

  :uberjar-name "gorilla-repl-ng-standalone.jar"

  ;; Those websocket exclusions are ugly but needed since "ring uberwar" does
  ;; not honor :provided
  :uberjar-exclusions [#".*javax/websocket.*" #".*javax/servlet.*"]

  ;; :jar-exclusions   [#"(?:^|\/)foo\/" #"(?:^|\/)demo\/" #"(?:^|\/)compiled.*\/" #"html$"]

  :ring {:war-exclusions [#"WEB-INF/lib/javax.websocket-api-1.0.jar"]
         :handler        gorilla-repl.route/redirect-route
         :servlet-class  gorilla_repl.RedirectServlet
         :servlet-name   redirect-servlet
         :uberwar-name   "gorilla-repl-ng.war"}

  :min-lein-version "2.5.0"

  :main ^:skip-aot gorilla-repl.core

  ;; :aot [gorilla-repl.servlet]

  ;; :jvm-opts ["-Xmx1g"]

  :java-source-paths ["src/java"]
  :source-paths ["src/clj" "src/cljc" "env/prod/clj"]
  :resource-paths ["resources" "target/cljsbuild"]

  ;; ["resources/public/js/compiled" "target"]
  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :minify-assets
  {:assets
   {"resources/gorilla-repl-client/gorilla.min.css" "resources/gorilla-repl-client/css"}
   #_:options #_{:linebreak    80
                 :optimization :advanced
                 :externs      ["jquery.min.js"]}}

  :prep-tasks ["javac" "compile" ["cljsbuild" "once"]]
  :env {:production true}

  :cljsbuild {:jar true
              :builds
                   {:app {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
                          :compiler     {:output-to       "target/cljsbuild/gorilla-repl-client/js/gorilla.js"
                                         :output-dir      "target/js/out"
                                         :asset-path      "/js/out"
                                         :foreign-libs    [{:file     "resources/gorilla-repl-client/js/codemirror/mode/clojure/clojure-parinfer.js"
                                                            :requires ["cljsjs.codemirror"]
                                                            :provides ["cljsjs.codemirror.mode.clojure-parinfer"]}
                                                           {:file     "resources/gorilla-repl-client/jslib/mousetrap-global-bind.min.js"
                                                            :requires ["cljsjs.mousetrap"]
                                                            :provides ["cljsjs.mousetrap-global-bind"]}
                                                           {:file     "resources/gorilla-repl-client/js/worksheetParser.js"
                                                            :provides ["gorilla-repl.worksheet-parser"]}]
                                         :main            gorilla-repl.prod
                                         :verbose         true
                                         :compiler-stats true
                                         :closure-defines {goog.DEBUG false}
                                         :elide-asserts   true
                                         :optimizations   :advanced
                                         :externs         ["src/cljs/gorilla-repl-externs.js"]
                                         :pretty-print    false
                                         :parallel-build  true}}}}


  :profiles {:dev     {:repl-options   {:init-ns gorilla-repl.repl}
                       :prep-tasks     ^:replace ["javac" "compile"]
                       :dependencies   [[figwheel-sidecar "0.5.8"]
                                        ;; [leiningen-core "2.6.1"] ;; project/read breaks clsjbuild
                                        [ring/ring-mock "0.3.0"]
                                        [ring/ring-devel "1.5.0"]
                                        [prone "1.1.2"]
                                        [org.clojure/tools.nrepl "0.2.12"]
                                        ;; Dirac or piggieback - there can only be one of them
                                        [binaryage/dirac "RELEASE"] ;; 0.6.7
                                        [com.cemerick/piggieback "0.2.1"]
                                        [lein-doo "0.1.7"]
                                        [re-frisk "0.2.2"]
                                        [day8.re-frame/test "0.1.0"]
                                        [devcards "0.2.2"
                                         :exclusions [org.clojure/tools.reader]]
                                        [pjstadig/humane-test-output "0.8.1"]
                                        ;; https://github.com/day8/re-frame-tracer
                                        [org.clojars.stumitchell/clairvoyant "0.2.0"]
                                        [day8/re-frame-tracer "0.1.1-SNAPSHOT"]
                                        [binaryage/devtools "0.8.2"]
                                        ;; Gorilla server side stuff
                                        [hiccup "1.0.5"]
                                        ;; https://github.com/clojure-numerics/expresso/issues/19
                                        [expresso "0.2.2-SNAPSHOT"]
                                        [instaparse "1.4.3"]
                                        [aysylu/loom "0.6.0"]
                                        [loom-gorilla "0.1.0"]
                                        [org.clojure/data.xml "0.0.8"]
                                        [incanter-gorilla "0.1.0"]
                                        [me.lomin/component-restart "0.1.1"]]

                       :source-paths   ^:replace ["src/clj" "src/cljc" "env/dev/clj"]

                       :resource-paths ^:replace ["resources" "target/cljsbuild" "env/dev/resources"]

                       :plugins        [[lein-doo "0.1.7"]
                                        [cider/cider-nrepl "0.14.0"]
                                        [org.clojure/tools.namespace "0.3.0-alpha2"
                                         :exclusions [org.clojure/tools.reader]]
                                        [refactor-nrepl "2.2.0"
                                         :exclusions [org.clojure/clojure]]]

                       :injections     [(require 'pjstadig.humane-test-output)
                                        (pjstadig.humane-test-output/activate!)]

                       :env            ^:replace {:dev true}

                       ;; app and devcards builds moved to figwheel.clj
                       :cljsbuild      {:builds
                                        {:test {:source-paths ["src/cljs" "src/cljc" "test/cljs"]
                                                :compiler     {:output-to      "target/test.js"
                                                               :main           gorilla-repl.doo-runner
                                                               :optimizations  :whitespace
                                                               :pretty-print   true
                                                               :parallel-build true
                                                               :foreign-libs   [{:file     "resources/gorilla-repl-client/js/codemirror/mode/clojure/clojure-parinfer.js"
                                                                                 :requires ["cljsjs.codemirror"]
                                                                                 :provides ["cljsjs.codemirror.mode.clojure-parinfer"]}
                                                                                {:file     "resources/gorilla-repl-client/jslib/mousetrap-global-bind.min.js"
                                                                                 :requires ["cljsjs.mousetrap"]
                                                                                 :provides ["cljsjs.mousetrap-global-bind"]}
                                                                                {:file     "resources/gorilla-repl-client/js/worksheetParser.js"
                                                                                 :provides ["gorilla-repl.worksheet-parser"]}]}}}}}

             :uberjar {:hooks       [minify-assets.plugin/hooks]
                       ;; :source-paths ["env/prod/clj"]
                       ;; :prep-tasks   ["javac" "compile" ["cljsbuild" "once"]]
                       ;; :env          {:production true}
                       :aot         :all
                       :omit-source true}

             ;; :cljs-jar  {:clean-targets ^{:protect false} ["run/resources/public/compiled_prod"] }
             }
  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}})
