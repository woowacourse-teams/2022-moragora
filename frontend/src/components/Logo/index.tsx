import * as S from './Logo.styled';
import logo from 'assets/logo.svg';

const Logo = () => {
  return (
    <S.LogoBox>
      <S.Title hidden>checkmate</S.Title>
      <S.LogoImage src={logo} alt="checkmate" />
    </S.LogoBox>
  );
};

export default Logo;
