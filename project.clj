(defproject org.pinkgorilla/gorilla-notebook "0.4.14"
  :description "A rich REPL for Clojure in the notebook style."
  :url "https://github.com/pink-gorilla/gorilla-notebook"
  :scm {:name "git" :url "https://github.com/pink-gorilla/gorilla-notebook"}
  :license {:name "MIT"}
  :min-lein-version "2.9.1"
  :min-java-version "1.11"
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]
;; Deps: clj, cljs or cljc - that is the question!

;; Use this to check depencencies:
;; lein deps :tree    shows if old versions are used (output in beginning)
;; lein ancient       shows outdated dependncies (more recent version available on clojars)

;; The first dependency determines the dependency version:
;; see: https://github.com/technomancy/leiningen/blob/stable/doc/FAQ.md
;; :exclusions can be used to fix this issue
;; [Z "1.0.9"]
;; [X "1.3.2"]
;;   [Z "2.0.1"]
;; ==> the direct dependency ([Z "1.0.9"]) is picked, as it is closest to the root.
;; [X "1.3.2"]
;;   [Z "2.0.1"]
;; [Y "1.0.5"]
;; [Z "2.1.3"]
;; ==> the dependency X comes first, and therefore [Z "2.0.1"] is picked

;; managed deendencies only define the version of a dependency,
;; if no dependeny needs them, then they are not included
  :managed-dependencies [; to avoid a :exclusion mess, we define certain versions numbers centrally
                         ; serialization libraries are dependencies o many libraries,
                         [org.clojure/core.memoize "0.8.2"]
                         [org.clojure/data.json "1.0.0"]
                         [org.clojure/data.fressian "1.0.0"]
                         [org.clojure/core.match "1.0.0"]
                         [com.cognitect/transit-clj "1.0.324"]
                         [com.cognitect/transit-cljs "0.8.256"]
                         [com.fasterxml.jackson.core/jackson-core "2.11.0.rc1"]
                         [cheshire "5.10.0"]
                         [com.taoensso/encore "2.119.0"]
                         ; patches to get most uptodate version for certain conflicts:
                         [commons-codec "1.12"] ; selmer and clj-http (via gorilla-explore)
                         [ring/ring-codec "1.1.1"] ; ring and compojure
                         [org.flatland/useful "0.11.6"] ; clojail and ring-middleware-format
                         ; pinkgorilla (enforce to use latest version of all projects)
                         [org.pinkgorilla/gorilla-middleware "0.2.21"]
                         [org.pinkgorilla/gorilla-renderable-ui "0.1.30"]
                         [org.pinkgorilla/gorilla-ui "0.1.27"]
                         [org.pinkgorilla/notebook-encoding "0.0.28"]
                         [org.pinkgorilla/gorilla-explore "0.1.20"]
                         [org.pinkgorilla/kernel-cljs-shadowdeps "0.0.12"]
                         [org.pinkgorilla/kernel-cljs-shadow "0.0.25"]
                         ; shadow-cljs
                         [thheller/shadow-cljs "2.8.94"]]

  :dependencies [;; CLOJURE ESSENTIAL
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.1.582"]
                 [org.clojure/tools.reader "1.3.2"]
                 [org.clojure/core.match "1.0.0"]

                 ;; CONFIGURATION / LOGGING / SYSTEM MANAGEMENT
                 [grimradical/clj-semver "0.3.0" :exclusions [org.clojure/clojure]]
                 ;; TODO: What logging do we actually want/need behind the scenes and what are we using directly ?
                 [org.slf4j/slf4j-api "1.7.29"]
                 [ch.qos.logback/logback-core "1.2.3"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [com.taoensso/timbre "4.10.0"]             ; clojurescript logging
                 ;; Things get very noise with slf4j-timbre - needs configuration
                 ;; [com.fzakaria/slf4j-timbre "0.3.2"]
                 [environ "1.1.0"]  ; Environment variables
                 [com.stuartsierra/component "0.4.0"]
                 [org.danielsz/system "0.4.3"
                  :exclusions [io.aviso/pretty]] ; newer version in com.taoensso/timbre
                 [de.otto/tesla-microservice "0.13.1"]

                 ;; ENCODING / SERIALIZATION
                 [com.taoensso/sente "1.15.0"]
                 ;; Chord needed for clojure?
                 [jarohen/chord "0.8.1"
                  :exclusions [com.cognitect/transit-clj
                               com.cognitect/transit-cljs]] ; websockets with core.async
                 [org.clojure/data.json] ; managed-version


                 ;; WEB SERVER
                 [ring "1.7.1"]
                 [ring-cors "0.1.13"]
                 [ring/ring-defaults "0.3.2"
                  :exclusions [javax.servlet/servlet-api]]
                 ;; [ring.middleware.logger "0.5.0"]
                 ;; [ring-webjars "0.1.1"]  ;; Although not matching servlet3 paths
                 [ring-middleware-format "0.7.4"]
                 ;;  ring-json introduces jackson along with its tail - but so does cljs-ajax :/
                 ;; [ring/ring-json "0.4.0"]
                 [javax.websocket/javax.websocket-api "1.1"]
                 [javax.servlet/javax.servlet-api "4.0.1"]
                 ;; [org.eclipse.jetty.websocket/websocket-server "9.4.12.v20180830"]
                 [de.otto/tesla-jetty "0.2.6"
                  :exclusions [org.eclipse.jetty/jetty-server
                               org.eclipse.jetty/jetty-servlet]]
                 ;; [de.otto/tesla-httpkit "1.0.1"]
                 [compojure "1.6.1"] ; Routing
                 [selmer "1.12.18"]
                 ;; Bringing it in here bc that is where the websocket "processors" come in
                 [info.sunng/ring-jetty9-adapter "0.12.5"]

                 ; Notebook Encoding / Exploration
                 [org.pinkgorilla/notebook-encoding] ; notebook encoding
                 [org.pinkgorilla/gorilla-explore] ; notebook exploration
                 [irresponsible/tentacles "0.6.6"] ; github api (needed by encoding and explore)
                 ;[clj-time "0.15.2"] ; needed for notebook exploration ui
                 [com.andrewmcveigh/cljs-time "0.5.2"] ;  notebook exploration ui

                 ; CLJ Kernel
                 [org.pinkgorilla/gorilla-middleware]
                 [clj-commons/pomegranate "1.2.0"] ; add-dependency in clj kernel TODO : Replace pomegranate with tools alpha
                 [org.clojure/tools.cli "1.0.194"]
                 ;[clojail "1.0.6"] ; Sandboxing - not sure whether we want to cope with this level of detail

                 ; PINKIE
                 [org.pinkgorilla/gorilla-renderable-ui] ; kernels (clj and cljs) needs renderable (cljs kernel is implemented in notebook)
                 [org.pinkgorilla/gorilla-ui] ; ui renderer impls

                 ; CLJS Kernel
                 [org.pinkgorilla/kernel-cljs-shadowdeps]
                 ;[org.pinkgorilla/kernel-cljs-shadowdeps
                 ; :exclusions [*/*]] ; add precompiled bundles via jar resources

                 ;; [com.rpl/specter "0.13.2"]
                 ]


  ;; REPLIKATIV
  ;  [io.replikativ/replikativ "0.2.4"]
  ;  [com.cognitect/transit-cljs "0.8.239" :scope "provided"]

  :plugins [[lein-shell "0.5.0"]
            [lein-environ "1.1.0"] ;; TODO Will likely be axed soon
            ;; tools.namespace "Unparsable namespace form:" ["parinfer-codemirror"]
            ;; quick hack is to temporarily rm .cljs files
            ;; [lein-hiera "1.1.0"] ; tools.namespace "Unparsable namespace form:" ["parinfer-codemirror"]
            ;; See: https://clojure.atlassian.net/browse/TNS-51
            ;; [walmartlabs/vizdeps "0.1.6"]
            [lein-ancient "0.6.15"]
            ;[lein-ring "0.12.5"]
            [lein-asset-minifier "0.4.6"
             :exclusions [org.clojure/clojure]]
            [min-java-version "0.1.0"]]

  ;; :uberjar-name "gorilla-notebook-standalone.jar"

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

  :main ^:skip-aot pinkgorilla.notebook-app.core

  ;; :aot [gorilla-repl.servlet]

  ;; :jvm-opts ["-Xmx1g"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.8" "-source" "1.8"]
  :source-paths ["src/clj" "env/prod/clj"]
  :test-paths ["test"]
  :resource-paths ["resources" "target/cljsbuild" "target/gen-resources"]

  ;; ["resources/public/js/compiled" "target"]
  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :minify-assets [[:css {:source "resources/gorilla-repl-client/css"
                         :target "resources/gorilla-repl-client/gorilla.min.css"}]]

  :prep-tasks ["javac"
               "compile"
               "build-tailwind-dev"
               "build-shadow-without-cljs-kernel"
               "build-shadow-with-cljs-kernel"
               ;; "build-shadow-pinkie"
               ]

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]

  ;; :classifiers
  #_{:standalone :uberjar
     :python     :python}
  :env {:production true}

  ;; We might chose to leverage the shell escape hatch to get out of dependency hell
  :aliases {"build-tailwind-dev"               ^{:doc "Build tailwind development."}
            ["shell" "npm" "run" "tailwind-development"]
            "build-shadow-ci"                  ^{:doc "Build shadow-cljs ci"}
            ["with-profile" "+cljs" "run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]
            "build-shadow-with-cljs-kernel"    ^{:doc "Build shadow-cljs with cljs kernel"}
            ["with-profile" "+cljs" "run" "-m" "shadow.cljs.devtools.cli" "compile" ":app-with-cljs-kernel"]
            "build-shadow-without-cljs-kernel" ^{:doc "Build shadow-cljs without cljs kernel"}
            ["with-profile" "+cljs" "run" "-m" "shadow.cljs.devtools.cli" "release" ":app-without-cljs-kernel"]
            "build-browser-test"               ^{:doc "Shadow-cljs browser test"}
            ["with-profile" "+cljs" "run" "-m" "shadow.cljs.devtools.cli" "compile" ":browser-test"]
            "watch-cards"                      ^{:doc "Shadow-cljs watch cards"}
            ["with-profile" "+cljs" "run" "-m" "shadow.cljs.devtools.cli" "watch" ":cards"]
            "test-js"                          ^{:doc "Test compiled JavaScript."}
            ["shell" "npm" "run" "test"]
            "clj-kondo"                        ^{:doc "Lint with clj-kondo"}
            ["run" "-m" "clj-kondo.main"]
            "lint"                             ^{:doc "Lint for dummies"}
            ["clj-kondo" "--lint" "src/clj/pinkgorilla"]
            "coverage"                             ^{:doc "Code coverage for dummies"}
            ["with-profile" "+cljs" "cloverage"]
            "bump-version"                     ^{:doc "Roll versions artefact version"}
            ["change" "version" "leiningen.release/bump-version"]
            "build-shadow-pinkie"
            ["with-profile" "+pinkie,+cljs" "run" "-m" "shadow.cljs.devtools.cli"  "compile" ":pinkie"]
            "watch-shadow-pinkie"
            ["with-profile" "+pinkie" "run" "-m" "shadow.cljs.devtools.cli"  "watch" ":pinkie"]
            "run-pinkie"
            ["with-profile" "+pinkie" "run" "-m" "pinkie.app"]
            "test-js-compile"                          ^{:doc "compile and Test JavaScript."}
            ["do" ["build-shadow-ci"] "test-js"]}


  :profiles {:devcljs {:source-paths   ^:replace ["src/clj" "env/dev/clj"]
                       :resource-paths ^:replace ["resources"
                                                  "target/cljsbuild"
                                                  "target/gen-resources"
                                                  "env/dev/resources"]
                       :env            ^:replace {:dev true}}

             :dev     {:repl-options   {:init-ns          pinkgorilla.repl
                                        :port             4001
                                        :nrepl-middleware [shadow.cljs.devtools.server.nrepl/middleware]}
                       :prep-tasks     ^:replace ["javac" "compile"]
                       :plugins        [;; [refactor-nrepl "2.2.0" :exclusions [org.clojure/clojure]]
                                        [lein-cljfmt "0.6.6"]
                                        [lein-cloverage "1.1.2"]]
                       :cloverage      {:codecov? true
                                        ;; In case we want to exclude stuff
                                        ;; :ns-exclude-regex [#".*util.instrument"]
                                        ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                        }
                       ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                       :cljfmt         {:indents {as->                [[:inner 0]]
                                                  with-debug-bindings [[:inner 0]]
                                                  merge-meta          [[:inner 0]]
                                                  try-if-let          [[:block 1]]}}
                       :dependencies   [;; [thheller/shadow-cljs "2.8.80"]  ;; Cannot overrides default deps here
                                        ;; [com.google.javascript/closure-compiler-unshaded "v20191027"]
                                        ;; Just moving shadow here blows up via :prep-tasks
                                        ;; Syntax error (NoSuchMethodError) compiling at (shadow/cljs/devtools/api.clj:1:1).
                                        ;; com.google.common.base.Preconditions.checkState
                                        ;; (ZLjava / lang/String ;Ljava/lang/Object;)V

                                        ;; [com.google.javascript/closure-compiler-unshaded "v20190325"]
                                        ;; [org.clojure/google-closure-library "0.0-20190213-2033d5d9"]
                                        ;; [com.bhauman/rebel-readline-cljs "0.1.4"]
                                        ;; [karma-reporter "3.1.0"]
                                        [day8.re-frame/test "0.1.5"] ; awb99 added this, so lein test would get re-frame/test dependencies
                                        [clj-kondo "2019.11.23"]
                                        [ring/ring-mock "0.4.0"]
                                        [ring/ring-devel "1.7.1"]
                                        [prone "2019-07-08"]
                                        [pjstadig/humane-test-output "0.10.0"]
                                        [instaparse "1.4.10"]
                                        [me.lomin/component-restart "0.1.2"]]

                       :source-paths   ^:replace ["src/clj" "test" "env/dev/clj"]

                       :resource-paths ^:replace ["resources"
                                                  "target/cljsbuild"
                                                  "target/gen-resources"
                                                  "env/dev/resources"]

                       :injections     [(require 'pjstadig.humane-test-output)
                                        (pjstadig.humane-test-output/activate!)]

                       :env            ^:replace {:dev true}}

             :pinkie    {:source-paths ["src/pinkie"]
                         :main ^:skip-aot pinkie.app
                         :dependencies [[thheller/shadow-cljs "2.8.80"]
                                        [thheller/shadow-cljsjs "0.0.21"]
                                        [bk/ring-gzip "0.3.0"] ; from oz
                                        [com.taoensso/encore "2.119.0"] ; needed by sente
                                        ;[com.taoensso/sente "1.13.1"] ; from oz
                                        [com.taoensso/sente "1.15.0"] ; already included above
                                        [aleph "0.4.6"] ; from oz
                                        ; gorilla:cljs profile
                                        [reagent "0.10.0"  ; was 0.8.1
                                         :exclusions [org.clojure/tools.reader
                                                      cljsjs/react
                                                      cljsjs/react-dom]]
                                        [re-frame "0.10.9"]
                                        [re-catch "0.1.4"]
                                        [prismatic/dommy "1.1.0"]
                                        [day8.re-frame/http-fx "0.1.6"] ; reframe based http requests
                                        [day8.re-frame/undo "0.3.3"]
                                        [day8.re-frame/re-frame-10x "0.6.2"]
                                        [secretary "1.2.3"]
                                        [re-com "2.8.0"]
                                        [org.pinkgorilla/kernel-cljs-shadow]]
                         :repl-options {:init-ns        pinkie.app
                                        :port             4003
                                        :nrepl-middleware [shadow.cljs.devtools.server.nrepl/middleware]}}

             :cljs    {:dependencies [[thheller/shadow-cljs "2.8.80"]
                                      ; cljs-ajax requires [com.cognitect/transit-cljxxx]
                                      ; awb99: if ajax is not here then chord will  require an older version and build will break
                                      [cljs-ajax "0.8.0"]   ; needed by reagent http-fx ??
                                      [prismatic/dommy "1.1.0"]
                                      [com.cemerick/url "0.1.1"]
                                      ;; CLOJURESCRIPT
                                      [com.google.javascript/closure-compiler-unshaded "v20191027"]
                                      [org.clojure/clojurescript "1.10.597"
                                       :scope "provided"
                                       :exclusions [com.google.javascript/closure-compiler-unshaded
                                                    org.clojure/google-closure-library
                                                    org.clojure/google-closure-library-third-party]]
                                      ;; CLJS KERNEL
                                      [org.pinkgorilla/kernel-cljs-shadow]
                                      [thheller/shadow-cljsjs "0.0.21"]

                                      ;; [cljs-tooling "0.2.0"]
                                      ;; https://github.com/bhauman/lein-figwheel/issues/612
                                      ;; [javax.xml.bind/jaxb-api "2.4.0-b180830.0359" :scope "provided"]
                                      [secretary "1.2.3"]   ; client side routing - TODO: Should likely be replaced by jux/bidi
                                      ;; [bidi "2.1.6"]

                                      [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ;; awb99: in encoding, and clj/cljs proof

                                      ;; REACT / REAGENT / REFRAME
                                      [reagent "0.10.0" ; was 0.8.1
                                       :exclusions [org.clojure/tools.reader
                                                    cljsjs/react
                                                    cljsjs/react-dom]]
                                      [re-com "2.8.0"]      ; reagent reuseable ui components
                                      [re-frame "0.10.9"]
                                      [day8.re-frame/http-fx "0.1.6"] ; reframe based http requests
                                      [day8.re-frame/undo "0.3.3"]
                                      [day8.re-frame/tracing "0.5.3"]
                                      [re-catch "0.1.4"]    ; exception handling for reagent components
                                      ;awb99: kee-frame seems to bring old dependencies?
                                      ;[kee-frame "0.3.3"] ; reframe with batteries - scroll fix, chains
                                      ;; TODO : Give fork (cssless) a spin!
                                      ;; [fork "1.2.3"]
                                      ;; [reagent-forms "0.5.27"] ;; Based on Bootstrap
                                      ;; [reagent-utils "0.2.0"]
                                      ;[district0x.re-frame/google-analytics-fx "1.0.0"
                                      ; :exclusions [re-frame]]
                                      [com.andrewmcveigh/cljs-time "0.5.2"]

                                      ;; actually dev
                                      [binaryage/dirac "RELEASE"] ;; 0.6.7
                                      ;[cider/piggieback "0.4.2"
                                      ; ;; :exclusions [org.clojure/clojurescript]
                                      ; ]
                                      [day8.re-frame/test "0.1.5"]
                                      [nubank/workspaces "1.0.13"]
                                      ;; devcards should be superceded by nubank, no?
                                      ;; [devcards "0.2.6" :exclusions [org.clojure/tools.reader]]
                                      ;; https://github.com/day8/re-frame-tracer
                                      ;; [org.clojars.stumitchell/clairvoyant "0.2.1"]
                                      [day8.re-frame/re-frame-10x "0.6.2"]
                                      [binaryage/devtools "1.0.0"]]}
             :uberjar {:hooks       [minify-assets.plugin/hooks]
                       :aot         :all
                       :omit-source true
                       :classifier  "standalone"}
             :python  {:dependencies [[cnuernber/libpython-clj "1.30"]]
                       ;; :uberjar-name "gorilla-notebook-standalone-with-python.jar"
                       }}
  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}})
