import Router from 'router';
import { css } from '@emotion/react';
import MobileLayout from 'components/layouts/MobileLayout';
import Header from 'components/layouts/Header';

const App = () => {
  return (
    <>
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
    </>
  );
};

export default App;
