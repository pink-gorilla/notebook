// Experimental npm build fragment
// http://blob.tomerweller.com/reagent-import-react-components-from-npm
/*
 [cljsjs.codemirror.addon.edit.closebrackets]
 [cljsjs.codemirror.addon.edit.matchbrackets]
 [cljsjs.codemirror.addon.runmode.runmode]
 [cljsjs.codemirror.addon.runmode.colorize]
 [cljsjs.codemirror.addon.hint.show-hint]
 [cljsjs.codemirror.mode.clojure]
 [cljsjs.codemirror.mode.clojure-parinfer]
 [cljsjs.codemirror.mode.markdown]
 [cljsjs.codemirror.mode.xml]
 */
window.deps = {
    'react' : require('react'),
    'react-dom' : require('react-dom'),
    /*
    'codemirror' : require('codemirror'),
    'codemirror/addon/edit/closebrackets': require('codemirror/addon/edit/closebrackets'),
    'codemirror/addon/edit/matchbrackets': require('codemirror/addon/edit/matchbrackets'),
    'codemirror/addon/runmode/runmode': require('codemirror/addon/runmode/runmode'),
    'codemirror/addon/runmode/colorize': require('codemirror/addon/runmode/colorize'),
    'codemirror/addon/hint/show-hint': require('codemirror/addon/hint/show-hint'),
    'codemirror/mode/clojure/clojure': require('codemirror/mode/clojure/clojure'),
    './codemirror/mode/clojure/clojure-parinfer': require('./codemirror/mode/clojure/clojure-parinfer'),
    */
// ERROR in ./src/npm-cljs/codemirror/mode/clojure/clojure-parinfer.js
// Module not found: Error: Cannot resolve 'file' or 'directory' ../../lib/codemirror in .../src/npm-cljs/codemirror/mode/clojure
    'codemirror/mode/xml/xml': require('codemirror/mode/xml/xml')
};
window.React = window.deps['react'];
window.ReactDOM = window.deps['react-dom'];
// window.CodeMirror = window.deps['codemirror'];
