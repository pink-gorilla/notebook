#!/bin/sh

# create basic pom
clojure -Spom -A:notebook

# write git tag version to pom
clojure -M:garamond -p

# re-add notebook deps to pom (garamond cannot do it)
clojure -Spom -A:notebook

# create jar
clojure -X:notebook:jar-bundel

# deploy jar to clojars
clojure -X:deploy-bundel