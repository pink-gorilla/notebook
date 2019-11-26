#!/bin/sh
# TODO: Ugly workaround
JAVA_OPTS="-Djava.security.policy=pinkgorilla.policy -agentlib:jdwp=transport=dt_socket,address=localhost:9002,server=y,suspend=n" \
  lein run -m pinkgorilla.repl

# JAVA_OPTS="-Djava.security.policy=pinkgorilla.policy -agentlib:jdwp=transport=dt_socket,address=localhost:9002,server=y,suspend=n" \
#    rlwrap lein repl
# lein run -m clojure.main script/run-repls.clj

