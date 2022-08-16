import React from 'react';
import * as S from './UserConfigPage.styled';
import Footer from 'components/layouts/Footer';
import Avatar from 'components/@shared/Avatar';
import MenuLink from 'components/@shared/MenuLink';
import NicknameInput from 'components/NicknameInput';
import useForm from 'hooks/useForm';

const UserConfigPage = () => {
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

  return (
    <>
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
        <S.MenuLinkBox>
          <MenuLink
            to=""
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
                  d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207"
                />
              </svg>
            }
            disabled
          >
            이메일 <S.EmailSpan>email@email.com</S.EmailSpan>
          </MenuLink>
          <MenuLink
            to="password"
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
                  d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"
                />
              </svg>
            }
          >
            비밀번호 변경
          </MenuLink>
          <MenuLink
            to="unregister"
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
                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                />
              </svg>
            }
          >
            회원 탈퇴
          </MenuLink>
        </S.MenuLinkBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default UserConfigPage;
