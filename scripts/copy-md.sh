#!/bin/sh

gorillamd="target/node_modules/public/gorillamd"

echo "copying markdown resources.."
mkdir -p  $gorillamd

cp README.md $gorillamd/notebook.md