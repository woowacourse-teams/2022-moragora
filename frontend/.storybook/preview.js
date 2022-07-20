import { initialize, mswDecorator } from 'msw-storybook-addon';
import { withRouter } from 'storybook-addon-react-router-v6';
import { ThemeProvider } from '@emotion/react';
import handlers from 'mocks/handlers';
import GlobalStyles from 'styles/GlobalStyles';
import { theme } from 'styles/themes/theme';

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
