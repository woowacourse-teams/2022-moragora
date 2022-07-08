import React from 'react';
import GlobalStyles from './styles/GlobalStyles';
import MobileLayout from './components/layouts/MobileLayout';
import Header from './components/layouts/Header';
import MeetingPage from './pages/MeetingPage';
import { css } from '@emotion/react';

const App = () => {
  return (
    <>
      <GlobalStyles />
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
    </>
  );
};

export default App;
