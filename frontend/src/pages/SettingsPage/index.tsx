import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from 'components/layouts/Footer';
import * as S from './SettingsPage.styled';
import Button from 'components/@shared/Button';
import ModalPortal from 'components/ModalPortal';
import ModalWindow from 'components/@shared/ModalWindow';
import { userContext } from 'contexts/userContext';
import Avatar from 'components/@shared/Avatar';
import NicknameInput from 'components/NicknameInput';
import useForm from 'hooks/useForm';

const SettingsPage = () => {
  const navigate = useNavigate();
  const userState = useContext(userContext);
  const [isModalOpened, setIsModalOpened] = useState(false);
  const { values, onSubmit, register, isSubmitting } = useForm();

  const handleNicknameValid: React.FormEventHandler<HTMLFormElement> = ({
    currentTarget,
  }) => {
    const formData = new FormData(currentTarget);
    const formDataObject = Object.fromEntries(formData.entries());

    console.log(formDataObject);
  };

  const handleNicknameError = () => {
    alert('error');
  };

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
          <form {...onSubmit(handleNicknameValid, handleNicknameError)}>
            <NicknameInput
              type="text"
              {...register('nickname', {
                defaultValue: 'unknown',
                onBlur: ({ target }) => {
                  target.form?.requestSubmit();
                },
                minLength: 1,
                maxLength: 15,
                pattern: '^([a-zA-Z0-9가-힣]){1,15}$',
                required: true,
                watch: true,
              })}
              nickname={values['nickname'] as string}
              disabled={isSubmitting}
            />
          </form>
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
