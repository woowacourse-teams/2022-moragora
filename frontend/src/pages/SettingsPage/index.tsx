import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from 'components/layouts/Footer';
import * as S from './SettingsPage.styled';
import Button from 'components/@shared/Button';
import ModalPortal from 'components/ModalPortal';
import ModalWindow from 'components/@shared/ModalWindow';
import { userContext } from 'contexts/userContext';
import Avatar from 'components/@shared/Avatar';
import MenuLink from 'components/@shared/MenuLink';

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
          <S.NicknameParagraph>unknown</S.NicknameParagraph>
        </S.ProfileBox>
        <S.ButtonBox>
          <MenuLink
            to="user-config"
            icon={
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="1rem"
                height="1rem"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M10 6H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V8a2 2 0 00-2-2h-5m-4 0V5a2 2 0 114 0v1m-4 0a2 2 0 104 0m-5 8a2 2 0 100-4 2 2 0 000 4zm0 0c1.306 0 2.417.835 2.83 2M9 14a3.001 3.001 0 00-2.83 2M15 11h3m-3 4h2"
                />
              </svg>
            }
          >
            회원정보 수정
          </MenuLink>
          <Button onClick={() => handleOpen()}>로그아웃</Button>
        </S.ButtonBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default SettingsPage;
