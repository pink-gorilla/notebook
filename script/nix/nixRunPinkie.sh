#!/bin/bash
# once we get sandbox-paths to work in nix, we will not have to this .m2 madness

function finish {
  mv ~/.m2/ ~/.m2-nix
  mv ~/.m2-host/ ~/.m2
}
trap finish EXIT

mv ~/.m2/ ~/.m2-host
mv ~/.m2-nix/ ~/.m2
nix-shell --pure -p leiningen --run 'lein run-pinkie'

