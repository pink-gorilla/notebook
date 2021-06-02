#!/bin/sh

# create basic pom
clojure -Spom -A:notebook

# write git tag version to pom
clojure -M:garamond -p

# create jar
clojure -X:jar-bundel

# deploy jar to clojars
clojure -X:deploy-bundel