#!/bin/sh
lein with-profile +cljs test
lein build-ci
./node_modules/karma/bin/karma start --single-run
