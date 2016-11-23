(defproject gorilla_sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :gorilla-options {:parinfer-mode :paredit
                    ;; parinfer nil
                    #_{:keymap {"command:worksheet:newBelow" "ctrl+b ctrl+t"
                              "command:worksheet:newAbove" "ctrl+b ctrl+q"}
                     :load-scan-exclude #{".git" ".svn"}}}
  :plugins [[org.clojars.deas/lein-gorilla "0.3.6-SNAPSHOT"]
            ;; [lein-gorilla "0.3.6"]
            ])
