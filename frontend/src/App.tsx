import React from 'react';
import GlobalStyles from './styles/GlobalStyles';
import MobileLayout from './components/layouts/MobileLayout';
import Header from './components/layouts/Header';
import MeetingPage from './pages/MeetingPage';
import { css, ThemeProvider } from '@emotion/react';
import { theme } from './styles/themes/theme';

const App = () => {
  return (
    <>
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
            <MeetingPage />
          </MobileLayout>
        </div>
        <div id="root-modal" />
      </ThemeProvider>
    </>
  );
};

export default App;
