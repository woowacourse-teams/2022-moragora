import Footer from 'components/layouts/Footer';
import * as S from './SettingsPage.styled';
import AvatarIconSVG from 'assets/avatar.svg';
import Button from 'components/@shared/Button';
import ModalPortal from 'components/ModalPortal';
import ModalWindow from 'components/@shared/ModalWindow';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SettingsPage = () => {
  const navigate = useNavigate();
  const [modalOpened, setModalOpened] = useState(false);

  const handleOpen = () => {
    setModalOpened(true);
  };
  const handleClose = () => {
    setModalOpened(false);
  };

  const handleConfirm = () => {
    localStorage.removeItem('accessToken');
    navigate('/');
  };

  return (
    <>
      {modalOpened && (
        <ModalPortal closePortal={handleClose}>
          <ModalWindow
            message="로그아웃하시겠습니까?"
            onConfirm={handleConfirm}
            onDismiss={handleClose}
          />
        </ModalPortal>
      )}
      <S.Layout>
        <S.AvatarBox>
          <S.AvatarBorder>
            <S.Image src={AvatarIconSVG} alt="avatar" />
          </S.AvatarBorder>
        </S.AvatarBox>
        <S.LogoutButtonBox>
          <Button onClick={() => handleOpen()}>로그아웃</Button>
        </S.LogoutButtonBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default SettingsPage;
