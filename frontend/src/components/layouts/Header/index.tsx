import { css } from '@emotion/react';
import * as S from './Header.styled';
import Logo from '../../Logo';

const Header = () => (
  <S.Layout>
    <S.BackwardButtonBox>
      <S.BackwardButton type="button">
        <S.ArrowLeftSVG
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2}
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M10 19l-7-7m0 0l7-7m-7 7h18"
          />
        </S.ArrowLeftSVG>
      </S.BackwardButton>
    </S.BackwardButtonBox>

    <Logo />

    <div
      css={css`
        display: flex;
        align-items: center;
        width: 24px;
      `}
    />
  </S.Layout>
);

export default Header;
