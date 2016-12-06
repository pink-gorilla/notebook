// Experimental npm build fragment
// http://blob.tomerweller.com/reagent-import-react-components-from-npm
window.deps = {
    'react' : require('react'),
    'react-dom' : require('react-dom')
};

window.React = window.deps['react'];
window.ReactDOM = window.deps['react-dom'];
