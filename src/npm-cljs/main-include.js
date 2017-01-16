// Experimental npm build fragment
// http://blob.tomerweller.com/reagent-import-react-components-from-npm
// We use cljsjs react because reagent depends on it and only god knows about the
// externs used by the latter. We want to be safe with advanced mode
window.deps = {
    // 'react-player': require('react-player')
};
