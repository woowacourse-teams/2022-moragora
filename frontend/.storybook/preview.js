import React from 'react';
import GlobalStyles from '../src/styles/GlobalStyles';
import { initialize, mswDecorator } from 'msw-storybook-addon';
import handlers from '../src/mocks/handlers';

// Initialize MSW
initialize();

// Provide decorators globally
export const decorators = [
  mswDecorator,
  (Story) => (
    <>
      <GlobalStyles />
      <Story />
    </>
  ),
];

export const parameters = {
  msw: {
    handlers,
  },
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};
