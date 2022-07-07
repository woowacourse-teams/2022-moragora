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
          width: 100vh;
        `}
      >
        <MobileLayout>
          <Header />
          <MeetingPage />
        </MobileLayout>
      </div>
    </>
  );
};

export default App;
