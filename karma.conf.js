module.exports = function (config) {
    config.set({
        browsers: ['ChromeHeadless'],
        // The directory where the output file lives
        basePath: 'target',
        // The file itself
        files: ['ci.js'],
        frameworks: ['cljs-test'],
        plugins: ['karma-cljs-test', 'karma-chrome-launcher'],
        colors: true,
        logLevel: config.LOG_INFO,
        client: {
            args: ["shadow.test.karma.init"],
            singleRun: true
        }
    })
};
/*
module.exports = function (config) {
    var root = 'target/cljsbuild/gorilla-repl-client/js/karma';// 'run/compiled/karma/test'; // same as :output-dir
    var junitOutputDir = process.env.CIRCLE_TEST_REPORTS || "target/cljsbuild/gorilla-repl-client/js/karma/junit";//run/compiled/karma/test/junit";

    config.set({
        frameworks: ['cljs-test'],
        browsers: ['Chrome'],
        files: [
            root + '/../gorilla_karma.js', // same as :output-to
            {pattern: root + '/../gorilla_karma.js.map', included: false}
        ],

        client: {
            args: ['gorilla_repl.karma_runner.run_karma']
        },

        // the default configuration
        junitReporter: {
            outputDir: junitOutputDir + '/karma', // results will be saved as $outputDir/$browserName.xml
            outputFile: undefined, // if included, results will be saved as $outputDir/$browserName/$outputFile
            suite: '' // suite will become the package name attribute in xml testsuite element
        },
        port: 9877
    })
};
*/
