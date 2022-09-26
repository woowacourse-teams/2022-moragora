import { defineConfig } from 'cypress';

export default defineConfig({
  reporter: 'list',
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
    },
  },
  video: false,
  screenshotOnRunFailure: false,
});
