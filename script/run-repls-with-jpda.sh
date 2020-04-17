#!/bin/sh
# export JAVA_CMD=/usr/lib/jvm/java-8-openjdk-amd64/bin/java
export JAVA_OPTS="-Djava.security.policy=pinkgorilla.policy -agentlib:jdwp=transport=dt_socket,address=localhost:9002,server=y,suspend=n"

# TODO: Ugly workaround
lein with-profile +cljs,+devcljs run -m pinkgorilla.repl $*
# ,+python
# JAVA_OPTS="-Djava.security.policy=pinkgorilla.policy -agentlib:jdwp=transport=dt_socket,address=localhost:9002,server=y,suspend=n" \
#    rlwrap lein repl
# lein run -m clojure.main script/run-repls.clj

