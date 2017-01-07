const webpack = require('webpack');
const path = require('path');

// const BUILD_DIR = path.resolve(__dirname, 'target', 'cljsbuild', 'gorilla-repl-client', 'js');
const BUILD_DIR = path.resolve(__dirname, 'resources', 'gorilla-repl-client', 'jslib');
const APP_DIR = path.resolve(__dirname, 'src', 'npm-cljs');

const config = {
    entry: `${APP_DIR}/main.js`,
    output: {
        path: BUILD_DIR,
        filename: 'webpack-bundle.js'
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify('production')
            }
        })
    ]
};

module.exports = config;
