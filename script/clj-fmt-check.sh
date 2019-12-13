#!/bin/sh
lein with-profile +cljs,+cljfmt cljfmt check
