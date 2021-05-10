(ns demo.views.stacktrace
  (:require [pinkgorilla.notebook-ui.eval-result.stacktrace :refer [stacktrace-table]]))

(def res-stacktrace
  {:path "NO_SOURCE_PATH"
   :file "NO_SOURCE_PATH"
   :file-url nil
   :column 1
   :line 1
   :id "778c1acf-01b2-426c-a866-a037e820b753"
   :class "clojure.lang.Compiler$CompilerException"
   :stacktrace [{:name "clojure.lang.Compiler/analyzeSeq", :file "Compiler.java", :line 7115, :class "clojure.lang.Compiler", :method "analyzeSeq", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler/analyze", :file "Compiler.java", :line 6789, :class "clojure.lang.Compiler", :method "analyze", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler/analyze", :file "Compiler.java", :line 6745, :class "clojure.lang.Compiler", :method "analyze", :type :java, :flags #{:dup :tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler$BodyExpr$Parser/parse", :file "Compiler.java", :line 6120, :class "clojure.lang.Compiler$BodyExpr$Parser", :method "parse", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler$FnMethod/parse", :file "Compiler.java", :line 5467, :class "clojure.lang.Compiler$FnMethod", :method "parse", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler$FnExpr/parse", :file "Compiler.java", :line 4029, :class "clojure.lang.Compiler$FnExpr", :method "parse", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler/analyzeSeq", :file "Compiler.java", :line 7105, :class "clojure.lang.Compiler", :method "analyzeSeq", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler/analyze", :file "Compiler.java", :line 6789, :class "clojure.lang.Compiler", :method "analyze", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler/eval", :file "Compiler.java", :line 7174, :class "clojure.lang.Compiler", :method "eval", :type :java, :flags #{:tooling :java}, :file-url nil}
                {:name "clojure.lang.Compiler/eval", :file "Compiler.java", :line 7132, :class "clojure.lang.Compiler", :method "eval", :type :java, :flags #{:dup :tooling :java}, :file-url nil}
                {:fn "eval", :method "invokeStatic", :ns "clojure.core", :name "clojure.core$eval/invokeStatic", :file "core.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/core.clj", :line 3214, :var "clojure.core/eval", :class "clojure.core$eval", :flags #{:clj}}
                {:fn "eval", :method "invoke", :ns "clojure.core", :name "clojure.core$eval/invoke", :file "core.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/core.clj", :line 3210, :var "clojure.core/eval", :class "clojure.core$eval", :flags #{:clj}}
                {:fn "evaluate/fn", :method "invoke", :ns "nrepl.middleware.interruptible-eval", :name "nrepl.middleware.interruptible_eval$evaluate$fn__13093/invoke", :file "interruptible_eval.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/nrepl/nrepl/0.7.0/nrepl-0.7.0.jar!/nrepl/middleware/interruptible_eval.clj", :line 91, :var "nrepl.middleware.interruptible-eval/evaluate", :class "nrepl.middleware.interruptible_eval$evaluate$fn__13093", :flags #{:tooling :clj}}
                {:fn "repl/read-eval-print/fn", :method "invoke", :ns "clojure.main", :name "clojure.main$repl$read_eval_print__9086$fn__9089/invoke", :file "main.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/main.clj", :line 437, :var "clojure.main/repl", :class "clojure.main$repl$read_eval_print__9086$fn__9089", :flags #{:clj}}
                {:fn "repl/read-eval-print", :method "invoke", :ns "clojure.main", :name "clojure.main$repl$read_eval_print__9086/invoke", :file "main.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/main.clj", :line 437, :var "clojure.main/repl", :class "clojure.main$repl$read_eval_print__9086", :flags #{:dup :clj}}
                {:fn "repl/fn", :method "invoke", :ns "clojure.main", :name "clojure.main$repl$fn__9095/invoke", :file "main.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/main.clj", :line 458, :var "clojure.main/repl", :class "clojure.main$repl$fn__9095", :flags #{:clj}}
                {:fn "repl", :method "invokeStatic", :ns "clojure.main", :name "clojure.main$repl/invokeStatic", :file "main.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/main.clj", :line 458, :var "clojure.main/repl", :class "clojure.main$repl", :flags #{:dup :clj}}
                {:fn "repl", :method "doInvoke", :ns "clojure.main", :name "clojure.main$repl/doInvoke", :file "main.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar!/clojure/main.clj", :line 368, :var "clojure.main/repl", :class "clojure.main$repl", :flags #{:clj}}
                {:name "clojure.lang.RestFn/invoke", :file "RestFn.java", :line 1523, :class "clojure.lang.RestFn", :method "invoke", :type :java, :flags #{:java}, :file-url nil}
                {:fn "evaluate", :method "invokeStatic", :ns "nrepl.middleware.interruptible-eval", :name "nrepl.middleware.interruptible_eval$evaluate/invokeStatic", :file "interruptible_eval.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/nrepl/nrepl/0.7.0/nrepl-0.7.0.jar!/nrepl/middleware/interruptible_eval.clj", :line 84, :var "nrepl.middleware.interruptible-eval/evaluate", :class "nrepl.middleware.interruptible_eval$evaluate", :flags #{:tooling :clj}}
                {:fn "evaluate", :method "invoke", :ns "nrepl.middleware.interruptible-eval", :name "nrepl.middleware.interruptible_eval$evaluate/invoke", :file "interruptible_eval.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/nrepl/nrepl/0.7.0/nrepl-0.7.0.jar!/nrepl/middleware/interruptible_eval.clj", :line 56, :var "nrepl.middleware.interruptible-eval/evaluate", :class "nrepl.middleware.interruptible_eval$evaluate", :flags #{:tooling :clj}}
                {:fn "interruptible-eval/fn/fn", :method "invoke", :ns "nrepl.middleware.interruptible-eval", :name "nrepl.middleware.interruptible_eval$interruptible_eval$fn__13119$fn__13123/invoke", :file "interruptible_eval.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/nrepl/nrepl/0.7.0/nrepl-0.7.0.jar!/nrepl/middleware/interruptible_eval.clj", :line 155, :var "nrepl.middleware.interruptible-eval/interruptible-eval", :class "nrepl.middleware.interruptible_eval$interruptible_eval$fn__13119$fn__13123", :flags #{:tooling :clj}}
                {:name "clojure.lang.AFn/run", :file "AFn.java", :line 22, :class "clojure.lang.AFn", :method "run", :type :java, :flags #{:java}, :file-url nil} {:fn "session-exec/main-loop/fn", :method "invoke", :ns "nrepl.middleware.session", :name "nrepl.middleware.session$session_exec$main_loop__13220$fn__13224/invoke", :file "session.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/nrepl/nrepl/0.7.0/nrepl-0.7.0.jar!/nrepl/middleware/session.clj", :line 190, :var "nrepl.middleware.session/session-exec", :class "nrepl.middleware.session$session_exec$main_loop__13220$fn__13224", :flags #{:tooling :clj}}
                {:fn "session-exec/main-loop", :method "invoke", :ns "nrepl.middleware.session", :name "nrepl.middleware.session$session_exec$main_loop__13220/invoke", :file "session.clj", :type :clj, :file-url "jar:file:/home/andreas/.m2/repository/nrepl/nrepl/0.7.0/nrepl-0.7.0.jar!/nrepl/middleware/session.clj", :line 189, :var "nrepl.middleware.session/session-exec", :class "nrepl.middleware.session$session_exec$main_loop__13220", :flags #{:tooling :clj}}
                {:name "clojure.lang.AFn/run", :file "AFn.java", :line 22, :class "clojure.lang.AFn", :method "run", :type :java, :flags #{:java}, :file-url nil}
                {:name "java.lang.Thread/run", :file "Thread.java", :line 834, :class "java.lang.Thread", :method "run", :type :java, :flags #{:java}, :file-url nil}]
   :location {:clojure.error/line 1
              :clojure.error/column 1
              :clojure.error/phase :compile-syntax-check
              :clojure.error/source "NO_SOURCE_PATH"
              :clojure.error/symbol "throw"}
   :message "Syntax error compiling throw at (1:1)."
   :session "4f44bee8-78d1-47da-a757-65e721a55a14"
   :data "{:clojure.error/phase :compile-syntax-check, :clojure.error/line 1, :clojure.error/column 1, :clojure.error/source \"NO_SOURCE_PATH\", :clojure.error/symbol throw}"})


(defn stacktrace-demo []
  [stacktrace-table (:stacktrace res-stacktrace)])


