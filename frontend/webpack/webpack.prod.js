const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const BundleAnalyzerPlugin =
  require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

const currentDate = new Date().toISOString().split('.')[0];

module.exports = merge(common, {
  mode: 'production',
  devtool: 'source-map',
  plugins: [
    new BundleAnalyzerPlugin({
      analyzerMode: 'disabled',
      generateStatsFile: true,
      statsFilename: `stats/stats-${currentDate}.json`,
    }),
  ],
});
