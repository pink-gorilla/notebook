#!/bin/bash
# once we get sandbox-paths to work in nix, we will not have to this .m2 madness

mv ~/.m2/ ~/.m2-host
mv ~/.m2-nix/ ~/.m2
nix-shell --pure -p leiningen --run './script/run-repls-with-jpda.sh'
mv ~/.m2/ ~/.m2-nix

