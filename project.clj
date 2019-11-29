(def foreign-libs [;{:file     "resources/gorilla-repl-client/jslib/cljs-include.js"
                   ; :provides ["gorilla-repl.webpack-include"
                   ;            ;; "cljsjs.react"
                   ;            ;; "cljsjs.react.dom"
                   ;            ;; "cljsjs.react.dom.server"
                   ;            ]
                   ; :requires ["cljsjs.react"
                   ;            "cljsjs.react.dom"] ;;  ... and use it externally in webpack
                   ; }
                   ;{:file     "resources/gorilla-repl-client/jslib/cljs-extern-empty.js"
                   ; :provides ["gorilla-repl.webpack-extern"]
                   ; }
                   {:file     "src/npm-cljs/codemirror/mode/clojure/clojure-parinfer.js"
                    :requires ["cljsjs.codemirror"]
                    :provides ["cljsjs.codemirror.mode.clojure-parinfer"]}
                   {:file     "resources/gorilla-repl-client/jslib/mousetrap-global-bind.min.js"
                    :requires ["cljsjs.mousetrap"]
                    :provides ["cljsjs.mousetrap-global-bind"]}])

(defproject org.pinkgorilla/gorilla-notebook "0.4.0-SNAPSHOT"
  :description "A rich REPL for Clojure in the notebook style."
  :url "https://github.com/pink-gorilla/gorilla-notebook"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/tools.reader "1.3.2"]
                 ;;  ring-json introduces jackson along with its tail
                 ;; [ring/ring-json "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 ;; [http-kit "2.2.0"]
                 ;; [cider/cider-nrepl "0.22.4"]
                 ;; [nrepl "0.6.0"]
                 ;; [cljs-tooling "0.2.0"]
                 [org.pinkgorilla/gorilla-middleware "0.2.2"]
                 [grimradical/clj-semver "0.3.0" :exclusions [org.clojure/clojure]]
                 [org.slf4j/slf4j-api "1.7.29"]
                 [ch.qos.logback/logback-core "1.2.3"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [com.taoensso/timbre "4.10.0"]
                 ;; Things get very noise with slf4j-timbre - needs configuration
                 ;; [com.fzakaria/slf4j-timbre "0.3.2"]
                 [cljs-ajax "0.8.0"]
                 [prismatic/dommy "1.1.0"]
                 ;; [selmer "1.0.3"] ;; django like templates - deps worth the hassle?
                 [reagent "0.8.1"
                  :exclusions [org.clojure/tools.reader
                               ; cljsjs/react
                               ; cljsjs/react-dom
                               ; ;; cljsjs/react-dom-server
                               ]]
                 [re-com "2.6.0"] ; reagent reuseable ui components
                 [day8.re-frame/http-fx "0.1.6"] ; reframe based http requests 
                 [day8.re-frame/undo "0.3.3"]
                 [re-catch "0.1.4"] ; exception handling for reagent components
                 ;awb99: kee-frame seems to bring old dependencies?
                 ;[kee-frame "0.3.3"] ; reframe with batteries - scroll fix, chains
                 
                 ;; Reagent uses React and may rely on cljsjs externs. So better not use a webpack version of
                 ;; React.
                 ;;
                 ;; React experimentally migrated to webpack/node
                 ;; To bundle cljsjs version, remove comments here and remove webpack_bundle.js from worksheet.html
                 ;; npm run build creates a fresh webpack_bundle.js
                 ;; [cljsjs/react "15.4.2-0"]
                 ;; [cljsjs/react-dom "15.4.2-0"]
                 ;;
                 ;; [flupot "0.4.0"]
                 ;; [reagent-forms "0.5.27"]
                 ;; [reagent-utils "0.2.0"]
                 ;; [hickory "0.6.0"] html -> hiccup as very last ressort only
                 ;; [replumb "0.2.4"] ; self hosted clojurescript
                 [org.webjars/MathJax "2.7.0"]              ;; TODO Not quite sure about value
                 [re-frame "0.10.9"]
                 [com.cemerick/url "0.1.1"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ;; awb99: in encoding, and clj/cljs proof
                 [org.clojure/tools.cli "0.4.2"]
                 [ring "1.7.1"
                  ;; :exclusions [ring/ring-jetty-adapter]
                  ]
                 [ring-cors "0.1.13"]
                 [ring/ring-defaults "0.3.2"
                  :exclusions [javax.servlet/servlet-api]]
                 ;; [ring.middleware.logger "0.5.0"]
                 ;; [ring-webjars "0.1.1"]                     ;; Although not matching servlet3 paths
                 [ring-middleware-format "0.7.4"]
                 [javax.websocket/javax.websocket-api "1.1"]
                 [javax.servlet/javax.servlet-api "4.0.1"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [environ "1.1.0"]
                 [com.stuartsierra/component "0.4.0"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 ;; https://github.com/bhauman/lein-figwheel/issues/612
                 ;; [javax.xml.bind/jaxb-api "2.4.0-b180830.0359" :scope "provided"]
                 [secretary "1.2.3"]
                 [cljsjs/parinfer "1.8.1-0"]
                 ;; Still helpful for externs!
                 [cljsjs/codemirror "5.44.0-1"]
                 ;[cljsjs/vega "2.6.0-0"]  2019-10-20 awb99 removed because it fucks up new vega
                 ;[cljsjs/d3geo "0.2.15-2"] 2019-10-20 awb99 removed because it fucks up new vega
                 ;[cljsjs/d3 "3.5.16-0"] 2019-10-20 awb99 removed because it fucks up new vega
                 [cljsjs/mousetrap "1.5.3-0"]
                 [cljsjs/marked "0.3.5-1"]
                 [com.taoensso/sente "1.14.0"]
                 [org.danielsz/system "0.4.3"]
                 [jarohen/chord "0.8.1"]                    ; websockets with core.async
                 [org.clojure/core.match "0.3.0"]
                 [de.otto/tesla-microservice "0.13.1"]
                 ;; [de.otto/tesla-httpkit "1.0.1"]
                 ;; Bringing it in here bc that is where the websocket "processors" come in
                 [com.bhauman/figwheel-repl "0.2.3"
                  ;; TODO: Trim to bare minimum
                  ;; :exclusions [*/*]
                  ]
                 [org.eclipse.jetty.websocket/websocket-server "9.4.12.v20180830"]
                 [de.otto/tesla-jetty "0.2.6"
                  :exclusions [org.eclipse.jetty/jetty-server]]
                 ;; [com.rpl/specter "0.13.2"]
                 [clojail "1.0.6"]
                 ;[cider/piggieback "0.4.2"
                 ; :exclusions [org.clojure/clojurescript]]
                 ;; TODO : Replace pomegranate with tools alpha
                 [com.cemerick/pomegranate "1.1.0"]         ; add-dependency in clj kernel
                 
                 ; cljs-kernel-shadow
                 [org.pinkgorilla/kernel-cljs-shadow "0.0.7"]
                 
                 ; cljs-kernel-klipse and its dependencies
                 ;[cljs-http "0.1.42"]
                 ;[appliedscience/js-interop "0.1.13"]
                 ;[viebel/gadjett "0.5.2"]
                 ;[viebel/klipse-clj "10.1.3"]               ; todo: remove parinfer dependency
                 
                 ;pinkgorilla sub projects
                 [org.pinkgorilla/gorilla-renderable "2.1.2"] ; kernels (clj and cljs) needs renderable (cljs kernel is implemented in notebook)
                 [org.pinkgorilla/encoding "0.0.18"]         ; notebook encoding
                 [irresponsible/tentacles "0.6.6"] ; github api 
                 ; notebook exploration:
                 [clj-time "0.11.0"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 ;[cljsjs/marked "0.3.5-0"] ; awb99: already required above
                 
                 ; ui plugins bundled with notebook
                 ;[awb99.fortune "0.0.1"]
                 ;[quil "3.1.0"]
                 ;[awb99/shapes "0.1.2"]
                 ]


  ;; REPLIKATIV
  ;  [io.replikativ/replikativ "0.2.4"]
  ;  [com.cognitect/transit-cljs "0.8.239" :scope "provided"]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-ring "0.12.5"]
            [lein-asset-minifier "0.4.6"
             :exclusions [org.clojure/clojure]]]

  :uberjar-name "gorilla-notebook-standalone.jar"

  ;; Those websocket exclusions are ugly but needed since "ring uberwar" does
  ;; not honor :provided
  ;; TODO: Not sure if this still applies as of 2019 - however, uberjar uses jetty9 which depends on servlet
  ;; :uberjar-exclusions [#".*javax/websocket.*" #".*javax/servlet.*"]
  ;; Cider. Does. Not. Play. With. AOT (cider.nrepl/delayed-handlers empty)
  :uberjar-exclusions [#"cider/nrepl.*\.class$"]

  ;; :jar-exclusions   [#"(?:^|\/)foo\/" #"(?:^|\/)demo\/" #"(?:^|\/)compiled.*\/" #"html$"]

  :ring {:war-exclusions [#"WEB-INF/lib/javax.websocket-api-1.0.jar"]
         :handler        pinkgorilla.route/redirect-handler
         :servlet-class  pinkgorilla.RedirectServlet
         :servlet-name   redirect-servlet
         :uberwar-name   "gorilla-notebook.war"}

  :min-lein-version "2.5.0"

  :main ^:skip-aot pinkgorilla.core

  ;; :aot [gorilla-repl.servlet]

  ;; :jvm-opts ["-Xmx1g"]
  :java-source-paths ["src/java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :source-paths ["src/clj" "env/prod/clj"]
  :test-paths ["test"]
  :resource-paths ["resources" "target/cljsbuild"]

  ;; ["resources/public/js/compiled" "target"]
  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :minify-assets [[:css {:source "resources/gorilla-repl-client/css"
                         :target "resources/gorilla-repl-client/gorilla.min.css"}]]

  :prep-tasks ["javac" "compile" ["cljsbuild" "once"]]
  :env {:production true}

  :cljsbuild {:jar true
              :builds
              {:with-cljs-kernel    {:source-paths ["src/clj" "env/prod/clj"]
                                     :compiler     {:output-to       "target/cljsbuild/gorilla-repl-client/js/gorilla.js"
                                                    :output-dir      "target/cljsbuild/gorilla-repl-client/js/modules"
                                                    :asset-path      "./js/modules"
                                                    :foreign-libs    ~foreign-libs
                                                    :main            pinkgorilla.prod
                                                    :elide-asserts   true
                                                    :optimizations   :none
                                                    :external-config {:gorilla/config {:with-cljs-kernel true}}
                                                    :infer-externs   true
                                                    :externs         ["src/cljs/gorilla-repl-externs.js"]
                                                    :pretty-print    false
                                                    :parallel-build  true}}
               :without-cljs-kernel {:source-paths ["src/clj" "env/prod/clj"]
                                     :compiler     {:output-to       "target/cljsbuild/gorilla-repl-client/js/gorilla-mock-cljs.js"
                                                    :foreign-libs    ~foreign-libs
                                                    :main            pinkgorilla.prod
                                                    :elide-asserts   true
                                                         ;; :verbose         true
                                                         ;; :compiler-stats  true
                                                         ;; :closure-defines {goog.DEBUG false}
                                                    :optimizations   :advanced
                                                    :external-config {:gorilla/config {:with-cljs-kernel false}}
                                                         ;; https://gist.github.com/swannodette/4fc9ccc13f62c66456daf19c47692799
                                                    :infer-externs   true
                                                    :externs         ["src/cljs/gorilla-repl-externs.js"]
                                                    :pretty-print    false
                                                    :parallel-build  true}}}}

  :doo {:build    "doo-test"
        :default  [#_:chrome #_:phantom :karma-phantom]
        :browsers [:chrome #_:firefox]
        :alias    {:default [:chrome-headless]}
        :karma    {:config {"proxies" {"/target" "./target"}}}
        :paths
        {;; :phantom "phantomjs --web-security=false"
         :karma "./node_modules/karma/bin/karma --port=9881 --no-colors"}}

  :profiles {:dev     {:repl-options   {:init-ns pinkgorilla.repl
                                        :port    4001
                                        :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                       :prep-tasks     ^:replace ["javac" "compile"]
                       :dependencies   [[com.bhauman/figwheel-main "0.2.3"]
                                        [com.bhauman/rebel-readline-cljs "0.1.4"]
                                        [karma-reporter "3.1.0"]
                                        ;; [leiningen-core "2.6.1"] ;; project/read breaks clsjbuild
                                        [ring/ring-mock "0.4.0"]
                                        [ring/ring-devel "1.7.1"]
                                        [prone "2019-07-08"]
                                        ;; Dirac or piggieback - there can only be one of them
                                        [binaryage/dirac "RELEASE"] ;; 0.6.7
                                        ;[cider/piggieback "0.4.2"
                                        ; ;; :exclusions [org.clojure/clojurescript]
                                        ; ]
                                        [doo "0.1.11"]
                                        [re-frisk "0.5.4.1"]
                                        [day8.re-frame/test "0.1.5"]
                                        [devcards "0.2.6"
                                         :exclusions [org.clojure/tools.reader
                                                      ;; cljsjs/react
                                                      ;; cljsjs/react-dom
                                                      ]]
                                        [pjstadig/humane-test-output "0.10.0"]
                                        ;; https://github.com/day8/re-frame-tracer
                                        ;; [org.clojars.stumitchell/clairvoyant "0.2.1"]
                                        ;; [day8/re-frame-tracer "0.1.1-SNAPSHOT"]
                                        [day8.re-frame/re-frame-10x "0.4.5"]
                                        [binaryage/devtools "0.9.10"]
                                        ;; Gorilla server side stuff
                                        [hiccup "1.0.5"]
                                        ;; https://github.com/clojure-numerics/expresso/issues/19
                                        [expresso "0.2.2"]
                                        [instaparse "1.4.10"]
                                        [org.clojure/data.xml "0.0.8"]
                                        [me.lomin/component-restart "0.1.2"]]

                       :source-paths   ^:replace ["src/clj" "test" "env/dev/clj"]

                       :resource-paths ^:replace ["resources" "target/cljsbuild" "env/dev/resources"]

                       :plugins        [[lein-doo "0.1.11"]
                                        #_[org.clojure/tools.namespace "0.3.1"
                                           :exclusions [org.clojure/tools.reader]]
                                        ;; [refactor-nrepl "2.2.0" :exclusions [org.clojure/clojure]]
                                        ]

                       :injections     [(require 'pjstadig.humane-test-output)
                                        (pjstadig.humane-test-output/activate!)]

                       :env            ^:replace {:dev true}

                       ;; app and devcards builds moved to figwheel.clj
                       :cljsbuild      {:builds
                                        ;; lein cljsbuild once doo-test; karma start
                                        ;; lein doo better than "plain" "lein cljsbuild once doo-test; karma start",
                                        ;; due to auto-build and so on
                                        ;; lein doo
                                        ;; Uncaught Error: js/React is missing
                                        {:doo-test {:source-paths ["src/clj" "test"]
                                                    :compiler     {:main           pinkgorilla.doo-runner
                                                                   ;; :main gorilla-repl.karma-runner
                                                                   :optimizations  :none
                                                                   :source-map     true
                                                                   :output-dir     "target/cljsbuild/gorilla-repl-client/js/doo"
                                                                   :output-to      "target/cljsbuild/gorilla-repl-client/js/gorilla_doo.js"
                                                                   ;; :asset-path breaks phantom (karma-phantom ok)
                                                                   ;; :asset-path     "base/target/cljsbuild/gorilla-repl-client/js/doo"
                                                                   ;; actually it is only a 404 /target/cljsbuild/gorilla-repl-client/js/doo/cljs_deps.js
                                                                   ;; caused by document.write in gorilla_doo.js which does not break functionality
                                                                   ;; can be prevented using
                                                                   ;; :optimizations  :whitespace
                                                                   ;; :source-map     "target/cljsbuild/gorilla-repl-client/js/gorilla_doo.js.map"
                                                                   :pretty-print   true
                                                                   :parallel-build true
                                                                   :foreign-libs   [;{:file     "resources/gorilla-repl-client/jslib/cljs-include.js"
                                                                                    ; :provides ["gorilla-repl.webpack-include"
                                                                                    ;            ;; "cljsjs.react"
                                                                                    ;            ;; "cljsjs.react.dom"
                                                                                    ;            ;; "cljsjs.react.dom.server"
                                                                                    ;            ]
                                                                                    ; :requires ["cljsjs.react"]}
                                                                                    ;{:file     "resources/gorilla-repl-client/jslib/cljs-extern.js"
                                                                                    ; :provides ["gorilla-repl.webpack-extern"]}
                                                                                    {:file     "src/npm-cljs/codemirror/mode/clojure/clojure-parinfer.js"
                                                                                     :requires ["cljsjs.codemirror"]
                                                                                     :provides ["cljsjs.codemirror.mode.clojure-parinfer"]}
                                                                                    {:file     "resources/gorilla-repl-client/jslib/mousetrap-global-bind.min.js"
                                                                                     :requires ["cljsjs.mousetrap"]
                                                                                     :provides ["cljsjs.mousetrap-global-bind"]}]}}}}}

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
