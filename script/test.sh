#!/bin/sh
lein with-profile +cljs test
lein build-shadow-ci
./node_modules/karma/bin/karma start --single-run
