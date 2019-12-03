(defproject org.pinkgorilla/gorilla-notebook "0.4.0"
  :description "A rich REPL for Clojure in the notebook style."
  :url "https://github.com/pink-gorilla/gorilla-notebook"
  :scm {:name "git" :url "https://github.com/pink-gorilla/gorilla-notebook"}
  :license {:name "MIT"}
  :dependencies [;; CLOJURE ESSENTIAL
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "0.5.527"]
                 [org.clojure/tools.reader "1.3.2"]
                 ;; [com.rpl/specter "0.13.2"]
                 [org.clojure/core.match "0.3.0"]

                 ;; Should actually be just a cljs dep not ending up in uberjar
                 [thheller/shadow-cljs "2.8.80"]

                 ; cljs-ajax requires [com.cognitect/transit-cljxxx]
                 ; awb99: if ajax is not here then chord will  require an older version and build will break
                 [cljs-ajax "0.8.0"]                        ; needed by reagent http-fx ??
                 
                 ;; CONFIGURATION / LOGGING / SYSTEM MANAGEMENT
                 [grimradical/clj-semver "0.3.0" :exclusions [org.clojure/clojure]]
                 [org.slf4j/slf4j-api "1.7.29"]
                 [ch.qos.logback/logback-core "1.2.3"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [com.taoensso/timbre "4.10.0"]             ; clojurescript logging
                 ;; Things get very noise with slf4j-timbre - needs configuration
                 ;; [com.fzakaria/slf4j-timbre "0.3.2"]
                 [environ "1.1.0"]
                 [com.stuartsierra/component "0.4.0"]
                 [org.danielsz/system "0.4.3"]
                 [de.otto/tesla-microservice "0.13.1"]

                 ;; ENCODING / SERIALIZATION
                 [com.taoensso/sente "1.15.0"]
                 [jarohen/chord "0.8.1"]                    ; websockets with core.async
                 [org.clojure/data.json "0.2.7"]
                 ;;  ring-json introduces jackson along with its tail
                 ;; [ring/ring-json "0.4.0"]
                 
                 ;; WEB SERVER
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
                 ;; [org.eclipse.jetty.websocket/websocket-server "9.4.12.v20180830"]
                 [de.otto/tesla-jetty "0.2.6"
                  :exclusions [org.eclipse.jetty/jetty-server
                               org.eclipse.jetty/jetty-servlet]]
                 ;; [de.otto/tesla-httpkit "1.0.1"]
                 [compojure "1.6.1"]                        ; Routing
                 
                 ;; Templating
                 [hiccup "1.0.5"]
                 [prismatic/dommy "1.1.0"]
                 ;; [selmer "1.0.3"] ;; django like templates - deps worth the hassle?
                 ;; [flupot "0.4.0"]
                 ;; [hickory "0.6.0"] html -> hiccup as very last ressort only
                 [com.cemerick/url "0.1.1"]

                 ;; CLOJURESCRIPT
                 [com.google.javascript/closure-compiler-unshaded "v20191027"]
                 [org.clojure/clojurescript "1.10.597"
                  :scope "provided"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 ;; CLJS KERNEL
                 [org.pinkgorilla/kernel-cljs-shadow "0.0.17"]
                 [thheller/shadow-cljsjs "0.0.21"]
                 ;; [cljs-tooling "0.2.0"]
                 ;; https://github.com/bhauman/lein-figwheel/issues/612
                 ;; [javax.xml.bind/jaxb-api "2.4.0-b180830.0359" :scope "provided"]
                 [secretary "1.2.3"]                        ; client side routing
                 
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ;; awb99: in encoding, and clj/cljs proof
                 
                 ;; REACT / REAGENT / REFRAME
                 [reagent "0.8.1"
                  :exclusions [org.clojure/tools.reader]]
                 ;; React experimentally migrated to webpack/node
                 ;; To bundle cljsjs version, remove comments here and remove webpack_bundle.js from worksheet.html
                 ;; npm run build creates a fresh webpack_bundle.js
                 ;; [cljsjs/react "15.4.2-0"]
                 ;; [cljsjs/react-dom "15.4.2-0"]
                 [re-com "2.6.0"]                           ; reagent reuseable ui components
                 [re-frame "0.10.9"]
                 [day8.re-frame/http-fx "0.1.6"]            ; reframe based http requests
                 [day8.re-frame/undo "0.3.3"]
                 ;[day8.re-frame/tracing "0.5.1"] ; da master does not want new dependencies
                 [re-catch "0.1.4"]                         ; exception handling for reagent components
                 ;awb99: kee-frame seems to bring old dependencies?
                 ;[kee-frame "0.3.3"] ; reframe with batteries - scroll fix, chains
                 ;; Reagent uses React and may rely on cljsjs externs. So better not use a webpack version of React.
                 ;; [reagent-forms "0.5.27"]
                 ;; [reagent-utils "0.2.0"]
                 
                 ;; UI Components
                 ;; [cljsjs/parinfer "1.8.1-0"]
                 ;; Still helpful for externs!
                 ;; [cljsjs/codemirror "5.44.0-1"]
                 ;[cljsjs/vega "2.6.0-0"]  2019-10-20 awb99 removed because it fucks up new vega
                 ;[cljsjs/d3geo "0.2.15-2"] 2019-10-20 awb99 removed because it fucks up new vega
                 ;[cljsjs/d3 "3.5.16-0"] 2019-10-20 awb99 removed because it fucks up new vega
                 ;;[cljsjs/mousetrap "1.5.3-0"]
                 ;; [cljsjs/marked "0.3.5-1"]
                 [org.webjars/MathJax "2.7.0"]              ;; TODO Not quite sure about value
                 
                 ;; Bringing it in here bc that is where the websocket "processors" come in
                 [info.sunng/ring-jetty9-adapter "0.12.5"]
                 #_[com.bhauman/figwheel-repl "0.2.3"
                    ;; TODO: Trim to bare minimum
                    ;; :exclusions [*/*]
                    ]

                 ;; *** PINK GORILLA ***
                 
                 ; CLJ Kernel
                 [org.pinkgorilla/gorilla-middleware "0.2.2"]
                 [com.cemerick/pomegranate "1.1.0"]         ; add-dependency in clj kernel TODO : Replace pomegranate with tools alpha
                 ;[cider/piggieback "0.4.2"
                 ; :exclusions [org.clojure/clojurescript]]
                 ;; [cider/cider-nrepl "0.22.4"]
                 ;; [nrepl "0.6.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [clojail "1.0.6"]                          ; sandboxing
                 
                 ;pinkgorilla sub projects
                 [org.pinkgorilla/gorilla-renderable "2.1.3"] ; kernels (clj and cljs) needs renderable (cljs kernel is implemented in notebook)
                 [org.pinkgorilla/encoding "0.0.18"]        ; notebook encoding
                 [irresponsible/tentacles "0.6.6"]          ; github api
                 ; notebook exploration:
                 [clj-time "0.15.2"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
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
  :javac-options ["-target" "1.8" "-source" "1.8"]
  :source-paths ["src/clj" "env/prod/clj"]
  :test-paths ["test"]
  :resource-paths ["resources" "target/cljsbuild"]

  ;; ["resources/public/js/compiled" "target"]
  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :minify-assets [[:css {:source "resources/gorilla-repl-client/css"
                         :target "resources/gorilla-repl-client/gorilla.min.css"}]]

  :prep-tasks ["javac"
               "compile"
               ;; "ci"
               "shadow-without-cljs-kernel"
               "shadow-with-cljs-kernel"]

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]]

  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases false}]]


  :env {:production true}

  ;; We might chose to leverage the shell escape hatch to get out of dependency hell
  :aliases {"ci"                         ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]
            "cards"                      ["run" "-m" "shadow.cljs.devtools.cli" "watch" ":cards"]
            "browser-test"               ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":browser-test"]
            "shadow-with-cljs-kernel"    ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":app-with-cljs-kernel"]
            "shadow-without-cljs-kernel" ["run" "-m" "shadow.cljs.devtools.cli" "release" ":app-without-cljs-kernel"]}

  :profiles {:dev     {:repl-options   {:init-ns          pinkgorilla.repl
                                        :port             4001
                                        :nrepl-middleware [;; cider.piggieback/wrap-cljs-repl
                                                           shadow.cljs.devtools.server.nrepl/middleware]}
                       :prep-tasks     ^:replace ["javac" "compile"]
                       :dependencies   [;; [thheller/shadow-cljs "2.8.80"]  ;; Cannot overrides default deps here
                                        ;; [com.google.javascript/closure-compiler-unshaded "v20191027"]
                                        ;; Just moving shadow here blows up via :prep-tasks
                                        ;; Syntax error (NoSuchMethodError) compiling at (shadow/cljs/devtools/api.clj:1:1).
                                        ;; com.google.common.base.Preconditions.checkState
                                        ;; (ZLjava / lang/String ;Ljava/lang/Object;)V

                                        ;; [com.google.javascript/closure-compiler-unshaded "v20190325"]
                                        ;; [org.clojure/google-closure-library "0.0-20190213-2033d5d9"]
                                        ;; [com.bhauman/figwheel-main "0.2.3"]
                                        ;; [com.bhauman/rebel-readline-cljs "0.1.4"]
                                        ;; [karma-reporter "3.1.0"]
                                        ;; [leiningen-core "2.6.1"] ;; project/read breaks clsjbuild
                                        [ring/ring-mock "0.4.0"]
                                        [ring/ring-devel "1.7.1"]
                                        [prone "2019-07-08"]
                                        ;; Dirac or piggieback - there can only be one of them
                                        [binaryage/dirac "RELEASE"] ;; 0.6.7
                                        ;[cider/piggieback "0.4.2"
                                        ; ;; :exclusions [org.clojure/clojurescript]
                                        ; ]
                                        ;; [doo "0.1.11"]
                                        [re-frisk "0.5.4.1"]
                                        [day8.re-frame/test "0.1.5"]
                                        [nubank/workspaces "1.0.13"]
                                        #_[devcards "0.2.6"
                                           :exclusions [org.clojure/tools.reader]]
                                        [pjstadig/humane-test-output "0.10.0"]
                                        ;; https://github.com/day8/re-frame-tracer
                                        ;; [org.clojars.stumitchell/clairvoyant "0.2.1"]
                                        ;; [day8/re-frame-tracer "0.1.1-SNAPSHOT"]
                                        [day8.re-frame/re-frame-10x "0.4.5"]
                                        [binaryage/devtools "0.9.11"]
                                        ;; Gorilla server side stuff
                                        [hiccup "1.0.5"]
                                        ;; https://github.com/clojure-numerics/expresso/issues/19
                                        [expresso "0.2.2"]
                                        [instaparse "1.4.10"]
                                        [org.clojure/data.xml "0.0.8"]
                                        [me.lomin/component-restart "0.1.2"]]

                       :source-paths   ^:replace ["src/clj" "test" "env/dev/clj"]

                       :resource-paths ^:replace ["resources" "target/cljsbuild" "env/dev/resources"]

                       :plugins        [
                                        ;; [refactor-nrepl "2.2.0" :exclusions [org.clojure/clojure]]
                                        ]

                       :injections     [(require 'pjstadig.humane-test-output)
                                        (pjstadig.humane-test-output/activate!)]

                       :env            ^:replace {:dev true}}

             :uberjar {:hooks       [minify-assets.plugin/hooks]
                       :aot         :all
                       :omit-source true}
             :python  {:dependencies [[cnuernber/libpython-clj "1.13"]]
                       :uberjar-name "gorilla-notebook-standalone-with-python.jar"}
             }
  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}})
