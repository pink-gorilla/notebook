#!/bin/sh
JAVA_OPTS="-Djava.security.policy=gorilla-repl.policy -agentlib:jdwp=transport=dt_socket,address=localhost:8002,server=y,suspend=n" rlwrap lein run -m clojure.main script/figwheel.clj

