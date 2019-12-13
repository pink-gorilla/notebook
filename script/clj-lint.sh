#!/bin/sh
#lein with-profile +cljs,+eastwood eastwood
lein with-profile +cljs clj-kondo --lint src
