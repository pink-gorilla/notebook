(defproject org.pinkgorilla/notebook "0.5.6-SNAPSHOT"
  :description "A clojure notebook web-app"
  :url "https://github.com/pink-gorilla/notebook"
  :license {:name "MIT"}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]

  :source-paths ["src"]
  :test-paths ["test"]
  :target-path  "target/jar"
  :resource-paths  ["resources" ; notebook-ui resources (css)
                    "target/node_modules"] ; css png resources from npm modules (codemirror themes)
  :clean-targets ^{:protect false} [:target-path
                                    [:demo :builds :app :compiler :output-dir]
                                    [:demo :builds :app :compiler :output-to]]

  :dependencies [[org.pinkgorilla/webly "0.2.43"]
                 [org.clojure/clojure "1.10.3"]
                 [org.clojure/core.async "1.3.618"]
                  ; cljs
                 [thi.ng/strf "0.2.2"]
                 [com.taoensso/timbre "5.1.2"] ; clj/cljs logging. awb99: this fucks up kernel-cljs-shadowdeps
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ;; awb99: in encoding, and clj/cljs proof
                 [day8.re-frame/http-fx "0.2.3"  ; reframe based http requests
                  :exclusions [[re-frame]]] ; a more modern reframe comes from webly
                 [day8.re-frame/undo "0.3.3"]
                 [re-com "2.13.2"]
                 ;pink-gorilla
                 [org.pinkgorilla/picasso "3.1.40"] ; used by nrepl-middleware + goldly cljs kernel
                 [org.pinkgorilla/gorilla-explore "0.2.62"] ; brings notebook-encoding
                 [org.pinkgorilla/nrepl-middleware "0.3.35"] ; brings picasso
                 [org.pinkgorilla/pinkie "0.3.3"]
                 [org.pinkgorilla/ui-markdown "0.0.8"]
                 [org.pinkgorilla/ui-code "0.0.12"]
                 [org.pinkgorilla/ui-site "0.0.4"]
                 [org.pinkgorilla/goldly "0.2.77"]]

  :profiles {:test {:source-paths ["src" "test"]
                    :test-paths   ["test"]}

             :bundel {:dependencies [[org.pinkgorilla/gorilla-ui "0.3.27"]
                                     [org.pinkgorilla/gorilla-plot "1.2.11"]
                                     [org.pinkgorilla/ui-quil "0.1.5"]]
                      :resource-paths ["target/webly" ; bundel
                                       ]}

             :demo {:dependencies []
                    :source-paths ["src"
                                   "profiles/demo/src"
                                   "test"]
                    :resource-paths ["target/webly" ; bundle
                                     "profiles/demo/resources"]}

             :dev  {:dependencies [[clj-kondo "2021.04.23"]
                                   [spec-provider "0.4.14"]]
                    :plugins      [[lein-cljfmt "0.6.6"]
                                   [lein-cloverage "1.1.2"]
                                   [lein-ancient "0.6.15"]
                                   [lein-resource "17.06.1"]
                                   [lein-shell "0.5.0"]]
                    :aliases      {"clj-kondo"
                                   ["run" "-m" "clj-kondo.main"]
                                   "bump-version" ^{:doc "Increases project.clj version number (used by CI)."}
                                   ["change" "version" "leiningen.release/bump-version"]}
                    :cloverage    {:codecov? true ; https://github.com/codecov/example-clojure
                                  ;; In case we want to exclude stuff
                                   :ns-exclude-regex [#"pinkgorilla.notebook-ui.app-bundel.*"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                   }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                    :cljfmt       {:indents {as->                [[:inner 0]]
                                             with-debug-bindings [[:inner 0]]
                                             merge-meta          [[:inner 0]]
                                             try-if-let          [[:block 1]]}}}}

  :aliases {"css"  ^{:doc "Copies certain npm package dependecies"}  ; copies codemirror theme-css, so they end up as resources
            ["shell" "./scripts/copy_res.sh"]
            "md"  ^{:doc "Copies markdown files to resources"}
            ["shell" "./scripts/copy-md.sh"]
            "prep-res"
            ["do" "css" ["md"]]

            "lint"  ^{:doc "Lint for dummies"}
            ["clj-kondo"
             "--config" "clj-kondo.edn"
             "--lint" "src"]

            ;; test

            "build-test"
            ["with-profile" "+demo" "run" "-m" "demo.app" "ci"]

            "test-run" ^{:doc "Runs unit tests. Does not build the bundle first.."}
            ["shell" "npm" "test"]

            "test-js" ^{:doc "Run Unit Tests. Will compile bundle first."}
            ["do" "build-test" ["test-run"]]

            ;; Notebook Demo

            "demo"
            ["with-profile" "+demo"
             "run" "-m" "demo.app" "watch"]

            ;; Notebook Bundel

            "notebook"
            ["with-profile" "+bundel"
             "run" "-m"
             "pinkgorilla.notebook-ui.app.app"]})
