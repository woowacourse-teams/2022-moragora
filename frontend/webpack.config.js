const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');

module.exports = {
  entry: './src/index.tsx',
  mode: 'development',
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        loader: 'ts-loader',
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
  resolve: {
    extensions: ['*', '.tsx', '.ts', '.js'],
    plugins: [
      new TsconfigPathsPlugin({
        extensions: ['*', '.tsx', '.ts', '.js'],
        baseUrl: './src/',
      }),
    ],
  },
  output: {
    path: path.resolve(__dirname, 'build'),
    publicPath: '/build',
    filename: '[name].bundle.js',
    assetModuleFilename: 'images/[hash][ext][query]',
    clean: true,
  },
  devServer: {
    static: {
      directory: path.join(__dirname, 'public'),
    },
    port: 8000,
    open: true,
    hot: true,
    historyApiFallback: true,
  },
  devtool: 'inline-source-map',
  plugins: [
    new HtmlWebpackPlugin({
      title: 'Moragora',
    }),
    new ESLintPlugin(),
  ],
};
