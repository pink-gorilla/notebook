// comment this out once :infer-externs true is in place
var Mousetrap = {};
Mousetrap.bindGlobal = function() {};

var MathJax = {};
MathJax.Hub.Config = function (cfg) {};
MathJax.Hub.Configured = function() {};
MathJax.Hub.Queue = function(item) {};

// Node/Webpack externs for advanced optimization - bare minimum
// externs inferences should be there soon
// cljsjs/vega has more

// 2019 10 20 awb99: remove this, as we use requirejs for vega
//var vg = {};
//vg.parse.spec = function (spec, callback) {};



/**
 * @dict
 */

// var shadow$provide = {};
//window.shadow$provide = {};

/**
 * @dict
 */
//var shadow$modules = {};
//window.shadow$modules = {};


//var shadow$umd$export = {};
//window.shadow$umd$export = {};