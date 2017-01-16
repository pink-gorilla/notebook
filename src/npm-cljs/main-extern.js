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
    /*
    'd3' : require('d3'),
    'd3-geo': require('d3-geo'),
    'vega': require('vega')
    */
    // For now, we still rely on cljsj codemirror externs
    /*
    'codemirror': require('codemirror'),
    'codemirror/addon/edit/closebrackets': require('codemirror/addon/edit/closebrackets'),
    'codemirror/addon/edit/matchbrackets': require('codemirror/addon/edit/matchbrackets'),
    'codemirror/addon/runmode/runmode': require('codemirror/addon/runmode/runmode'),
    'codemirror/addon/runmode/colorize': require('codemirror/addon/runmode/colorize'),
    'codemirror/addon/hint/show-hint': require('codemirror/addon/hint/show-hint'),
    'codemirror/mode/clojure/clojure': require('codemirror/mode/clojure/clojure'),
    'codemirror/mode/clojure/markdown': require('codemirror/mode/markdown/markdown'),
    'codemirror/mode/clojure/clojure-parinfer': require('./codemirror/mode/clojure/clojure-parinfer')
    */
    // 'codemirror/mode/xml/xml': require('codemirror/mode/xml/xml')
// ERROR in ./src/npm-cljs/codemirror/mode/clojure/clojure-parinfer.js
// Module not found: Error: Cannot resolve 'file' or 'directory' ../../lib/codemirror in .../src/npm-cljs/codemirror/mode/clojure
};
// window.vg = window.deps['vega'];
// window.CodeMirror = window.deps['codemirror'];

/*
 var opts = {
 lineNumbers: false,
 matchBrackets: true,
 autoCloseBrackets: "()[]{}\"\"",
 lineWrapping: true,
 keyMap: "gorilla",
 mode: "clojure"
 };*/
/*
 #_(aset js/CodeMirror "keyMap" "default" "Shift-Tab" "indentLess")

 (def cm-default-opts {:text/x-clojure  {:cm-opts (merge cm-default-opts-common
 {:mode "clojure"})}
 :text/x-markdown {:cm-opts (merge cm-default-opts-common
 {:mode "text/x-markdown"})}})
 */
