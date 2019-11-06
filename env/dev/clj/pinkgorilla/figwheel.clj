(ns pinkgorilla.figwheel
    (:require
      [pinkgorilla.handle :as handle]
      [pinkgorilla.route :as route]
      [pinkgorilla.jetty9-ws-relay :as ws-relay]))

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

(def dev-ws-processor (ws-relay/ws-processor route/nrepl-handler))

#_(handle/set-config :project
                   {:gorilla-options
                    {:editor {:text/x-clojure {:opts    [:paren-mode {:parinfer-mode :paren-mode}]
                                               :cm-opts {:mode "clojure-parinfer"}}}}}
                   ;; nil
                   ;; :indent-mode
                   )

(handle/set-config :keymap {})

(def foreign-libs
  [
   ;{:file     "resources/gorilla-repl-client/jslib/cljs-include.js"
   ; :provides ["gorilla-repl.webpack-include"
   ;            ;; "cljsjs.react"
   ;            ;; "cljsjs.react.dom"
   ;            ;; "cljsjs.react.dom.server"
   ;            ]
   ; :requires ["cljsjs.react"
   ;            "cljsjs.react.dom"]}
   ;{:file     "resources/gorilla-repl-client/jslib/cljs-extern-empty.js"
   ; :provides ["gorilla-repl.webpack-extern"]}
   {:file     "src/npm-cljs/codemirror/mode/clojure/clojure-parinfer.js"
    :requires ["cljsjs.codemirror"]
    :provides ["cljsjs.codemirror.mode.clojure-parinfer"]}
   {:file     "resources/gorilla-repl-client/jslib/mousetrap-global-bind.min.js"
    :requires ["cljsjs.mousetrap"]
    :provides ["cljsjs.mousetrap-global-bind"]}
   ])

;; We could still pull some paths from project.clj when using read-raw
;; or sys/fetch-config

(defn main-config
      []
      {:id      "app"
       :options {:main            'pinkgorilla.dev
                 :devcards        true
                 ;; :source-paths    ["src/cljs" "src/cljc" "env/dev/cljs"]
                 :output-to       "target/cljsbuild/gorilla-repl-client/js/gorilla.js",
                 :output-dir      "target/cljsbuild/gorilla-repl-client/js/out"
                 :asset-path      "/js/out",
                 :optimizations   :none,
                 :source-map      true
                 ;; :preloads             [devtools.preload]
                 ;; :external-config      {:devtools/config {:features-to-install :all}}
                 :pretty-print    true
                 :parallel-build  true
                 :verbose         false
                 :foreign-libs    foreign-libs
                 :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true}
                 :preloads        ['day8.re-frame-10x.preload]
                 :external-config {:gorilla/config {:with-cljs-kernel true}}
                 }
       :config  { :auto-testing true
                 ;; :open-url         "http://100.115.92.204:3449/worksheet.html"
                 :extra-main-files {:devcards {:main 'pinkgorilla.cards}
                                    ;; :tests    {:main pinkgorilla.core-test}
                                    }
                 :ring-handler        'pinkgorilla.dev-handle/dev-handler
                 :ring-server-options {:port       3449
                                       ;; :host       "100.115.92.204"
                                       ;; The following "abuses" knowledge that figwheel main
                                       ;; leverages ring-jetty9-adapter under the covers
                                       :websockets {"/repl" dev-ws-processor}}
                 ;; :http-server-root  "public" ;; does not matter
                 ;; :server-port      3449
                 ;; :nrepl-port          7002
                 ;; :nrepl-middleware    ["cider.piggieback/wrap-cljs-repl"
                 ;;                      "cider.nrepl/cider-middleware"]
                 :css-dirs            ["resources/gorilla-repl-client/css"]
                 :open-file-command   "open-in-intellij"
                 :watch-dirs          ["src/cljs" "src/cljc" "test/cljs" "env/dev/cljs"]
                 ;; :debug            true
                 }                                          ; an options map of figwheel.main config options
       })

#_(defn sidecar-config
        []
        {:figwheel-options {:ring-handler      'pinkgorilla.dev-handle/dev-handler
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
                                           {:main           'pinkgorilla.dev
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
                                           {:main                 'pinkgorilla.cards
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
