const { join } = require('path');
const { merge } = require('webpack-merge');
const common = require('./webpack.common');

module.exports = merge(common, {
  mode: 'development',
  devServer: {
    static: {
      directory: join(__dirname, '../public'),
    },
    port: 3000,
    hot: true,
    compress: true,
    client: {
      overlay: {
        errors: true,
        warnings: true,
      },
    },
    historyApiFallback: true,
  },
});
