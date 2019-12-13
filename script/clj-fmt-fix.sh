#!/bin/sh
lein with-profile +cljs,+cljfmt cljfmt fix
