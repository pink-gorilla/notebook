


## Other clojure projects that use codemirror
- reepl https://github.com/jaredly/reepl/blob/4adf48a7e1/src/reepl/code_mirror.cljs
reepl has parinfer
- saite https://github.com/jsa-aerial/saite/blob/master/src/cljs/aerial/saite/codemirror.cljs 
  saite has paredit
- https://github.com/metosin/komponentit/blob/master/src/cljs/komponentit/codemirror.cljs
  metosin makes clean apis
- https://gist.github.com/isaksky/a96476fa51a10ba903b7c6bdcb13c85b

 
## TODO

- "swapDoc" (instance: CodeMirror, oldDoc: Doc)
    This is signalled when the editor's document is replaced using the swapDoc method.
  use this to change buffers


-  setting this class' height style to auto will make the editor resize to fit its content (it is recommended to also set the viewportMargin option to Infinity when doing this.
-  