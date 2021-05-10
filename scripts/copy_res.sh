#!/bin/sh

cpr () {
  sourcedir="node_modules/$1"
  source="$sourcedir/$2"
  targetdir="target/node_modules/public/$1" 
  if [ -d $sourcedir ]; then
     echo "copying $source ==> $targetdir"
     mkdir -p $targetdir
     cp $source $targetdir
  else 
    echo "ERROR: $sourcedir does not exist."
  fi
}

#mkdir -p target/node_modules/public/codemirror/lib
# cp node_modules/codemirror/lib/*.css  target/node_modules/public/codemirror/lib
cpr "codemirror/lib" "*.css" 

mkdir -p target/node_modules/public/codemirror/theme
# cp node_modules/codemirror/theme/*.css  target/node_modules/public/codemirror/theme
cpr "codemirror/theme" "*.css" 

#mkdir -p target/node_modules/public/highlight.js/styles
# cp node_modules/highlight.js/styles/*.css  target/node_modules/public/highlight.js/styles
cpr "highlight.js/styles" "*.css"

cpr "prosemirror-view/style" "*.css"
cpr "prosemirror-menu/style" "*.css"
