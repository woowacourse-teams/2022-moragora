import React from 'react';
import GlobalStyles from '../src/styles/GlobalStyles';
import { initialize, mswDecorator } from 'msw-storybook-addon';
import handlers from '../src/mocks/handlers';
import { ThemeProvider } from '@emotion/react';
import { theme } from '../src/styles/themes/theme';
import { withRouter } from 'storybook-addon-react-router-v6';

// Initialize MSW
initialize();

// Provide decorators globally
export const decorators = [
  mswDecorator,
  withRouter,
  (Story) => (
    <>
      <GlobalStyles />
      <ThemeProvider theme={theme}>
        <Story />
      </ThemeProvider>
    </>
  ),
];

export const parameters = {
  msw: {
    handlers,
  },
  reactRouter: {
    routePath: '*',
  },
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};
