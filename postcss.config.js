// https://tailwindcss.com/docs/using-with-preprocessors/#build-time-imports
module.exports = {
    plugins: [
        require('postcss-import'),
        require('tailwindcss'),
        require('autoprefixer'),
    ]
}
