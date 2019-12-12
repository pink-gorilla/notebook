#!/bin/sh
lein with-profile +cljs clj-kondo --lint src
