#!/bin/sh

clojure -X:ci
# :profile '"ci"'
npm test