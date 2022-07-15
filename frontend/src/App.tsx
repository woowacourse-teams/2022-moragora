import React from 'react';
import GlobalStyles from './styles/GlobalStyles';
import MobileLayout from './components/layouts/MobileLayout';
import Header from './components/layouts/Header';
import { css, ThemeProvider } from '@emotion/react';
import { theme } from './styles/themes/theme';
import { BrowserRouter } from 'react-router-dom';
import Router from 'router';

const App = () => {
  return (
    <BrowserRouter>
      <GlobalStyles />
      <ThemeProvider theme={theme}>
        <div
          css={css`
            width: 100vw;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
          `}
        >
          <MobileLayout>
            <Header />
            <Router />
          </MobileLayout>
        </div>
        <div id="root-modal" />
      </ThemeProvider>
    </BrowserRouter>
  );
};

export default App;
