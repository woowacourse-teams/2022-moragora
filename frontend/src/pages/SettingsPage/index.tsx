import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from 'components/layouts/Footer';
import * as S from './SettingsPage.styled';
import Button from 'components/@shared/Button';
import ModalPortal from 'components/ModalPortal';
import ModalWindow from 'components/@shared/ModalWindow';
import { userContext } from 'contexts/userContext';
import Avatar from 'components/@shared/Avatar';
import EditIconSVG from 'assets/Edit.svg';
import { css } from '@emotion/react';

const SettingsPage = () => {
  const navigate = useNavigate();
  const userState = useContext(userContext);
  const [isModalOpened, setIsModalOpened] = useState(false);

  const handleOpen = () => {
    setIsModalOpened(true);
  };
  const handleClose = () => {
    setIsModalOpened(false);
  };

  const handleConfirm = () => {
    userState?.logout();
    navigate('/');
  };

  return (
    <>
      {isModalOpened && (
        <ModalPortal closePortal={handleClose}>
          <ModalWindow
            message="로그아웃하시겠습니까?"
            onConfirm={handleConfirm}
            onDismiss={handleClose}
          />
        </ModalPortal>
      )}
      <S.Layout>
        <S.ProfileBox>
          <Avatar />
          <div
            css={css`
              position: relative;
              right: -0.6rem;
              display: flex;
              gap: 0.2rem;
            `}
          >
            <S.NicknameInput
              placeholder={userState?.user?.nickname || 'unknown'}
            />
            <S.EditIconImg css={css``} src={EditIconSVG} />
          </div>
        </S.ProfileBox>
        <S.LogoutButtonBox>
          <Button onClick={() => handleOpen()}>로그아웃</Button>
        </S.LogoutButtonBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default SettingsPage;
