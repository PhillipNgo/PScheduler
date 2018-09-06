/* eslint-disable */

var webpack = require('webpack');
var path = require('path');
var UglifyJsPlugin = require('uglifyjs-webpack-plugin');

var CONTENT_DIR = path.resolve(__dirname, 'src/main/resources/static');
var APP_DIR = path.resolve(__dirname, 'src/main/js');

var frontendServerPort = 3000;
var backendServerPort = 8080;

var config = {
  mode: 'development',
  entry: APP_DIR + '/index.jsx',
  output: {
    path: CONTENT_DIR,
      filename: 'bundle.js',
  },
  module: {
    rules: [{
      test: /\.jsx?$/,
        include: APP_DIR,
        use: 'babel-loader',
        resolve: {
        extensions: ['.js', '.jsx'],
      },
    }]
  },
  optimization: {
    minimizer: [
      new UglifyJsPlugin({
        sourceMap: true,
      }),
    ],
  },
  plugins: [
    new webpack.DefinePlugin({
      devPorts: {
        backend: backendServerPort,
      },
    }),
  ],
  devServer: {
    port: frontendServerPort,
    contentBase: CONTENT_DIR,
  },
};

module.exports = config;
