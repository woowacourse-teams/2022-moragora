import * as S from './Logo.styled';
import logo from '../../assets/logo.svg';

const Logo = () => {
  return (
    <S.LogoBox>
      <S.Title hidden>moragora</S.Title>
      <S.LogoImage src={logo} alt="moragora" />
    </S.LogoBox>
  );
};

export default Logo;
