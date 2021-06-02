#!/bin/sh

# create basic pom
clojure -Spom

# write git tag version to pom
clojure -M:garamond -p

# create jar
clojure -X:jar


