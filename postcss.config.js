// https://tailwindcss.com/docs/using-with-preprocessors/#build-time-imports
module.exports = {
    plugins: [
        require('postcss-import'),
        require('tailwindcss'),
        require('autoprefixer')({
            overrideBrowserslist: [
                "last 1 chrome version",
                "firefox > 73",
                "last 1 safari version"
                //, "ie > 10"
            ]
        })
    ]
};


