import { defineConfig } from 'cypress';
const webpackConfig = require('./webpack/webpack.test');

console.log(webpackConfig);

export default defineConfig({
  reporter: 'junit',
  reporterOptions: {
    mochaFile: 'test-result.xml',
  },
  e2e: {
    baseUrl: 'http://localhost:3000',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
  component: {
    devServer: {
      framework: 'react',
      bundler: 'webpack',
      webpackConfig,
    },
  },
  video: false,
  screenshotOnRunFailure: false,
});
