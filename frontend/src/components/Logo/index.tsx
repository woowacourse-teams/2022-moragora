import * as S from './Logo.styled';
import logo from '../../assets/logo.svg';
import { css } from '@emotion/react';

const Logo = () => {
  return (
    <S.LogoBox>
      <S.Title hidden>moragora</S.Title>
      <img
        src={logo}
        css={css`
          height: 100%;
        `}
        alt="moragora"
      />
    </S.LogoBox>
  );
};

export default Logo;
