(ns pinkgorilla.kernel.browser
  ;; (:require [replumb.core :as replumb])
  )

#_(defn handle-result!
    [console result]
    (let [write-fn (if (replumb/success? result) console/write-return! console/write-exception!)]
      (write-fn console (replumb/unwrap-result result))))

#_(defn cljs-read-eval-print!
    [console repl-opts user-input]
    (replumb/read-eval-call repl-opts
                            (partial handle-result! console)
                            user-input))

#_(defn cljs-console-did-mount
    [console-opts]
    (js/$
      (fn []
        (let [repl-opts (merge (replumb/options :browser
                                                ["/src/cljs" "/js/compiled/out"]
                                                io/fetch-file!)
                               {:warning-as-error true
                                :verbose          true})
              jqconsole (console/new-jqconsole "#cljs-console" console-opts)]
          (cljs-console-prompt! jqconsole repl-opts)))))
