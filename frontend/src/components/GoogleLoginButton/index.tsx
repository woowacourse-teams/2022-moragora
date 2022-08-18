import { GOOGLE_AUTH_URI } from 'consts/auth';
import * as S from './GoogleLoginButton.styled';
import googleIconSVG from 'assets/google-icon.svg';
import { css } from '@emotion/react';

const GoogleLoginButton = () => {
  return (
    <S.Layout
      type="button"
      onClick={() => {
        location.replace(GOOGLE_AUTH_URI);
      }}
    >
      <img
        src={googleIconSVG}
        alt="google icon"
        css={css`
          width: 1rem;
          height: 1rem;
        `}
      />
      구글로 로그인 하기
    </S.Layout>
  );
};

export default GoogleLoginButton;
