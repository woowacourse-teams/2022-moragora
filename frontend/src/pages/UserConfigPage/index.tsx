import React, { useContext, useState } from 'react';
import { Navigate } from 'react-router-dom';
import * as S from './UserConfigPage.styled';
import Footer from 'components/layouts/Footer';
import Avatar from 'components/@shared/Avatar';
import MenuLink from 'components/@shared/MenuLink';
import NicknameInput from 'components/NicknameInput';
import { userContext } from 'contexts/userContext';
import useMutation from 'hooks/useMutation';
import { updateNicknameApi } from 'apis/userApis';

const UserConfigPage = () => {
  const user = useContext(userContext);

  if (!user?.accessToken) {
    return <Navigate to="/" />;
  }

  const [nickname, setNickname] = useState(user.user?.nickname ?? 'unknown');
  const nicknameUpdateMutation = useMutation(
    updateNicknameApi(user.accessToken),
    {
      onMutate: ({ nickname }) => {
        setNickname(nickname);
      },
      onError: (e) => {
        setNickname(user.user?.nickname ?? 'unknown');
        alert(e);
      },
      onSuccess: () => {
        user.getLoginUserData();
        alert('닉네임이 변경되었습니다.');
      },
    }
  );

  const handleNicknameValid: React.FormEventHandler<HTMLFormElement> = async (
    e
  ) => {
    e.preventDefault();
    const { currentTarget } = e;
    const formData = new FormData(currentTarget);
    const formDataObject = Object.fromEntries(formData.entries()) as {
      nickname: string;
    };

    if (formDataObject.nickname === user.user?.nickname) {
      return;
    }

    await nicknameUpdateMutation.mutate(formDataObject);
  };

  return (
    <>
      <S.Layout>
        <S.ProfileBox>
          <Avatar />
          <form onSubmit={handleNicknameValid}>
            <NicknameInput
              type="text"
              name="nickname"
              value={nickname}
              onChange={({ currentTarget }) => {
                setNickname(currentTarget.value);
              }}
              onBlur={({ currentTarget }) => {
                currentTarget.form?.requestSubmit();
              }}
              maxLength={15}
              pattern="^([a-zA-Z0-9가-힣]){1,15}$"
              required
              nickname={nickname}
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
            이메일 <S.EmailSpan>{user.user?.email ?? 'unknown'}</S.EmailSpan>
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
