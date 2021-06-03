#!/bin/sh


echo clojure -Sdeps \'$(cat bundel.edn)\' -X:notebook
clojure -Sdeps "$(cat bundel.edn)" -X:jar-bundel
