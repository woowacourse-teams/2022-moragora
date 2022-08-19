import { GOOGLE_AUTH_URI } from 'consts/auth';
import * as S from './GoogleLoginButton.styled';
import googleIconSVG from 'assets/google-icon.svg';

const GoogleLoginButton = () => {
  return (
    <S.Layout
      type="button"
      onClick={() => {
        location.replace(GOOGLE_AUTH_URI);
      }}
    >
      <S.Icon src={googleIconSVG} alt="google icon" />
      구글로 로그인 하기
    </S.Layout>
  );
};

export default GoogleLoginButton;
