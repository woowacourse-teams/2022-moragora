const { join, resolve } = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const dotenv = require('dotenv');
const { DefinePlugin, EnvironmentPlugin } = require('webpack');
const FaviconsWebpackPlugin = require('favicons-webpack-plugin');

dotenv.config();

module.exports = {
  mode: 'development',
  entry: join(__dirname, '../src/index.tsx'),
  devtool: 'eval-source-map',
  output: {
    publicPath: '/',
    path: resolve(__dirname, '../build'),
    filename: '[name].[contenthash].js',
    assetModuleFilename: 'images/[hash][ext]',
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
      meta: {
        description: {
          name: 'description',
          content: '출첵을 간편하게 - 출결관리 서비스 체크메이트',
        },
        keywords: {
          name: 'keywords',
          content: '우아한테크코스, 출석 체크, 출결, 근태, 모임',
        },
        'og:type': { name: 'og:type', content: 'website' },
        'og:url': { name: 'og:url', content: 'https://checkmate.today' },
        'og:title': { name: 'og:title', content: '체크메이트' },
        'og:description': {
          name: 'og:description',
          content: '출첵을 간편하게 - 출결관리 서비스 체크메이트',
        },
        'og:locale': { name: 'og:locale', content: 'en_US' },
        'twitter:card': { name: 'twitter:card', content: 'summary' },
        'twitter:title': { name: 'twitter:title', content: '체크메이트' },
        'twitter:description': {
          name: 'twitter:description',
          content: '출첵을 간편하게 - 출결관리 서비스 체크메이트',
        },
      },
    }),
    new ESLintPlugin(),
    new DefinePlugin({
      'process.env.API_SERVER_HOST': JSON.stringify(
        process.env.API_SERVER_HOST
      ),
      'process.env.CLIENT_ID': JSON.stringify(process.env.CLIENT_ID),
    }),
    new EnvironmentPlugin(['API_SERVER_HOST', 'CLIENT_ID']),
    new FaviconsWebpackPlugin({
      logo: 'public/logo.png',
      manifest: 'public/manifest.json',
    }),
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
