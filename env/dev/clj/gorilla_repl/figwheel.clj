(ns gorilla-repl.figwheel
  (:require [gorilla-repl.handle :as handle]
            [gorilla-repl.route :as route]
            [gorilla-repl.jetty9-ws-relay :as ws-relay]))

;; project/read (which seems to be required be merge)
;; messes up cljs compiler
#_(def config
    (-> (project/read)
        (project/read-raw "project.clj")
        (project/merge-profiles {:dev [:dev]})
        project/project-with-profiles
        cfg/config
        ))

;; (pprint (sys/fetch-config)))
(handle/set-config :project
                   {:gorilla-options
                    {:editor {:text/x-clojure {:opts    [:paren-mode {:parinfer-mode :paren-mode}]
                                               :cm-opts {:mode "clojure-parinfer"}}}}}
                   ;; nil
                   ;; :indent-mode
                   )

(handle/set-config :keymap {})

(def foreign-libs
  [{:file     "resources/gorilla-repl-client/jslib/cljs-include.js"
    :provides ["gorilla-repl.webpack-include"
               ;; "cljsjs.react"
               ;; "cljsjs.react.dom"
               ;; "cljsjs.react.dom.server"
               ]
    :requires ["cljsjs.react"
               "cljsjs.react.dom"]}
   {:file     "resources/gorilla-repl-client/jslib/cljs-extern-empty.js"
    :provides ["gorilla-repl.webpack-extern"]}
   {:file     "src/npm-cljs/codemirror/mode/clojure/clojure-parinfer.js"
    :requires ["cljsjs.codemirror"]
    :provides ["cljsjs.codemirror.mode.clojure-parinfer"]}
   {:file     "resources/gorilla-repl-client/jslib/mousetrap-global-bind.min.js"
    :requires ["cljsjs.mousetrap"]
    :provides ["cljsjs.mousetrap-global-bind"]}
   {:file     "./resources/gorilla-repl-client/js/worksheetParser.js"
    :provides ["gorilla-repl.worksheet-parser"]}])

;; We could still pull some paths from project.clj when using read-raw
;; or sys/fetch-config

(defn main-config
  []
  {
   :id      "app"
   :options {:main           'gorilla-repl.dev
             :source-paths   ["src/cljs" "src/cljc" "env/dev/cljs"]
             :output-to      "target/cljsbuild/gorilla-repl-client/js/gorilla.js",
             :output-dir     "target/cljsbuild/gorilla-repl-client/js/out"
             :asset-path     "/js/out",
             :optimizations  :none,
             :source-map     true
             ;; :preloads             [devtools.preload]
             ;; :external-config      {:devtools/config {:features-to-install :all}}
             :pretty-print   true
             :parallel-build true
             :verbose        true
             :foreign-libs   foreign-libs
             }
   :config  {:ring-handler        'gorilla-repl.dev-handle/dev-handler
             :ring-server-options {:port       3449
                                   ;; The following "abuses" knowledge that figwheel main
                                   ;; leverages ring-jetty9-adapter under the covers
                                   :websockets {"/repl" (ws-relay/ws-processor route/nrepl-handler)}}
             ;; :http-server-root  "public" ;; does not matter
             ;; :server-port      3449
             :nrepl-port          7002
             :nrepl-middleware    ["cemerick.piggieback/wrap-cljs-repl"
                                   "cider.nrepl/cider-middleware"]
             :css-dirs            ["resources/gorilla-repl-client/css"]
             :open-file-command   "open-in-intellij"
             ;; :debug            true
             }                                              ; an options map of figwheel.main config options
   })

#_(defn sidecar-config
    []
    {:figwheel-options {:ring-handler      'gorilla-repl.dev-handle/dev-handler
                        ;; :http-server-root  "public" ;; does not matter
                        ;; :server-port      3449
                        :nrepl-port        7002
                        :nrepl-middleware  ["cemerick.piggieback/wrap-cljs-repl"
                                            "cider.nrepl/cider-middleware"
                                            ;; "dirac.nrepl/middleware"
                                            ;; "refactor-nrepl.middleware/wrap-refactor"
                                            ]
                        :css-dirs          ["resources/gorilla-repl-client/css"]
                        ;; https://github.com/bhauman/lein-figwheel/wiki/Running-figwheel-in-a-Cursive-Clojure-REPL
                        :open-file-command "open-in-intellij"
                        ;; :debug            true
                        }
     ;;:figwheel-options nil,
     :all-builds
                       [{:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"],
                         :id           "app",
                         :compiler
                                       {:main           'gorilla-repl.dev
                                        :output-to      "target/cljsbuild/gorilla-repl-client/js/gorilla.js",
                                        :output-dir     "target/cljsbuild/gorilla-repl-client/js/out"
                                        :asset-path     "/js/out",
                                        :optimizations  :none,
                                        :source-map     true
                                        ;; :preloads             [devtools.preload]
                                        ;; :external-config      {:devtools/config {:features-to-install :all}}
                                        :pretty-print   true
                                        :parallel-build true
                                        :foreign-libs   foreign-libs
                                        }}
                        {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                         :id           "devcards"
                         :figwheel     {:devcards true}
                         :compiler
                                       {:main                 'gorilla-repl.cards
                                        :output-to            "target/cljsbuild/gorilla-repl-client/js/gorilla_devcards.js"
                                        :output-dir           "target/cljsbuild/gorilla-repl-client/js/devcards_out"
                                        :asset-path           "js/devcards_out"
                                        :optimizations        :none
                                        :source-map           true
                                        :source-map-timestamp true
                                        ;; :preloads             [devtools.preload]
                                        ;; :external-config      {:devtools/config {:features-to-install :all}}
                                        :pretty-print         true
                                        :parallel-build       true
                                        :foreign-libs         foreign-libs
                                        }}
                        ]
     :build-ids        ["app" "devcards"]}
    )
