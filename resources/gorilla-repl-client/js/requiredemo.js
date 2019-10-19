
// awb99: this demo implements our jsscript/render function
// it naively renders the data as JSON into the dom node.
// Useful to keep this in case requirejs makes problems.

define([], function () {
    return {
       render: function (id, data) {
           // id is the id of the element into which output is rendered to
          console.log("requiredemo is rendering to id: " + id);
          var domElement = document.getElementById(id);   

          var p = document.createElement ('p');
          domElement.appendChild (p);
          var json = JSON.stringify(data);
          var textnode = document.createTextNode (json);  
          p.appendChild (textnode);
          //require(['vega-embed'], function(vegaEmbed) {
          //  let spec = data;
          //  vegaEmbed('#uuid-%s', spec, {defaultStyle:true}).catch(console.warn);
          //}, function(err) {
          //   console.log('Failed to load');
          //});
       }
    }
});