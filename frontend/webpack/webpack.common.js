const { join, resolve } = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const dotenv = require('dotenv');
const { DefinePlugin, EnvironmentPlugin } = require('webpack');

dotenv.config();

module.exports = {
  mode: 'development',
  entry: join(__dirname, '../src/index.tsx'),
  devtool: 'eval-source-map',
  output: {
    path: resolve(__dirname, '../build'),
    filename: '[name].bundle.js',
    assetModuleFilename: 'images/[hash][ext][query]',
    clean: true,
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: join(__dirname, '../public/index.html'),
    }),
    new ESLintPlugin(),
    new DefinePlugin({
      'process.env.API_SERVER_HOST': JSON.stringify(
        process.env.API_SERVER_HOST
      ),
    }),
    new EnvironmentPlugin(['API_SERVER_HOST']),
  ],
  resolve: {
    extensions: ['*', '.tsx', '.ts', '.jsx', '.js'],
    plugins: [
      new TsconfigPathsPlugin({
        extensions: ['*', '.tsx', '.ts', '.jsx', '.js'],
        baseUrl: './src/',
      }),
    ],
  },
};
