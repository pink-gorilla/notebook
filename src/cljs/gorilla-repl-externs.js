// comment this out once :infer-externs true is in place
var Mousetrap = {};
Mousetrap.bindGlobal = function() {};

// Node/Webpack externs for advanced optimization - bare minimum
// externs inferences should be there soon
// cljsjs/vega has more
var vg = {};
vg.parse.spec = function (spec, callback) {};

