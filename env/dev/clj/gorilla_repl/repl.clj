(ns gorilla-repl.repl
  (:require [cider.piggieback :as pback]
            [cljs.build.api]
            [cljs.repl]
            [cljs.repl.node]
            [clojure.core.async :refer [go]]
            [gorilla-repl.core :as core]
            [figwheel.main.api :refer [start start-join cljs-repl]]
            [clojure.pprint :refer [pprint]]
            [gorilla-repl.figwheel :as gfw :refer [main-config]]
            [gorilla-repl.system :as gsys]
            )
  #_(:use
      ;; ring.server.standalone
      [ring.middleware file-info file]))

;; (defonce server (atom nil))

;; (start-figwheel! (config))

;; (ra/print-config)
;; (cljs-repl)

#_(defn get-handler []
    ;; #'app expands to (var app) so that when we reload our code,
    ;; the server is forced to re-resolve the symbol in the var
    ;; rather than having its own copy. When the root binding
    ;; changes, the server picks it up without having to restart.
    (-> #'app
        ; Makes static assets in $PROJECT_DIR/resources/public/ available.
        (wrap-file "resources")
        ; Content-Type, Content-Length, and Last Modified headers for files in body
        (wrap-file-info)))

#_(defn start-server
    "used for starting the server in development mode from REPL"
    [& [port]]
    (let [port (if port (Integer/parseInt port) 3000)]
      (reset! server
              (serve (get-handler)
                     {:port         port
                      :auto-reload? true
                      :join?        false}))
      (println (str "You can view the site at http://localhost:" port))))

#_(defn stop-server []
    (.stop @server)
    (reset! server nil))

#_(cljs.build.api/build "src"
                        {:main      'hello-world.core
                         :output-to "out/main.js"
                         :verbose   true})

#_(defn start-cljs-repl
    []
    (pback/cljs-repl (cljs.repl.node/repl-env)
                     :watch "in"
                     ;; :reader
                     :output-dir "out"))

#_(defn run-cljs
    []
    (core/run-gorilla-server {})
    (start-cljs-repl))
#_(go
    (cljs.repl/repl (cljs.repl.node/repl-env)
                    ;; :watch "in"
                    :reader
                    :output-dir "out"))

(def m {:content 1
        :b       2
        :c       3
        :d       4})
(apply dissoc m [:content :b :c])
