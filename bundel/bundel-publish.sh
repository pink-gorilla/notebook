#!/bin/sh

clojure -Spom
clojure -X:jar
clojure -X:deploy
