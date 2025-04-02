const path = require('path');
const webpack = require('webpack');
const CopyPlugin = require("copy-webpack-plugin");

module.exports = {
  mode: 'production',
  entry: './index.js',
  output: {
    path: path.join(__dirname, 'dist'),
    publicPath: '/public',
    filename: 'final.js',
    clean: true
  },
  target: 'node',
  plugins: [
    new webpack.IgnorePlugin({ resourceRegExp: /^pg-native$/ }),
    new CopyPlugin({
      patterns: [
        { from: "config.json", to: "." },
      ]
    })
  ]
};