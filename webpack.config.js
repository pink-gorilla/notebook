const webpack = require('webpack');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, 'resources', 'gorilla-repl-client', 'jslib');
const APP_DIR = path.resolve(__dirname, 'src', 'npm-cljs');

const config = {
    // https://medium.com/@rajaraodv/webpack-the-confusing-parts-58712f8fcad9#.4fkfk3zgf
    entry: {
        // The include build requires cljsjs.react
        "cljs-include": `${APP_DIR}/main-include.js`,
        // This is all the other stuff from npm cljs libraries(!) do not(!) depend upon
        // We need to take care of externs ourself
        "cljs-extern": `${APP_DIR}/main-extern.js`
    },
    output: {
        path: BUILD_DIR,
        filename: '[name].js'
    },
    // Because we take cljsjs versions because reagent
    externals: {
        "react": "React",
        "react-dom": "ReactDOM",
        "react-dom/server": "ReactDOMServer"
        // window.ReactDOMServer = window.deps['react-dom/server'];
        // require("jquery") is external and available
        //  on the global var jQuery
        // "jquery": "jQuery"
    },/*
    resolve: {
        extensions: ['', '.js', '.css']
    },*//*
    module: {
        rules: [
            {
                test: /\.json$/,
                use: 'json-loader'
            }
        ]
    },
    */
    plugins: [
        // Uses production build of react
        /*
         new webpack.DefinePlugin({
         'process.env': {
         'NODE_ENV': JSON.stringify('production')
         }
         })*/

    ]/*,
    node: {
        fs: "empty"
    }*/
};

module.exports = config;
