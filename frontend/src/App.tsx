import GlobalStyles from './styles/GlobalStyles';
import MobileLayout from './components/layouts/MobileLayout';
import Header from './components/layouts/Header';
import MeetingPage from './pages/MeetingPage';
import { css } from '@emotion/react';
import { useState } from 'react';
import ModalPortal from './components/ModalPortal';
import ModalWindow from './components/@shared/ModalWindow';

const App = () => {
  const [modalOpened, setModalOpened] = useState(false);

  const handleOpen = () => {
    setModalOpened(true);
  };

  const handleClose = () => {
    setModalOpened(false);
  };

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
      {modalOpened && (
        <ModalPortal closePortal={handleClose}>
          <ModalWindow />
        </ModalPortal>
      )}
      <div id="root-modal" />
    </>
  );
};

export default App;
