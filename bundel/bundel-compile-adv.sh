#!/bin/sh

# npm-install has to run, because shadow-cljs 3release· does not update package.json
# in comparison shadow-cljs "watch" does update package.json
clojure -X:notebook :profile '"npm-install"'
clojure -X:notebook :profile '"release-adv"'
