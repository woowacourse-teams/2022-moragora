import React from 'react';
import { css } from '@emotion/react';
import * as S from './Header.styled';
import Logo from '../../Logo';
import ChevronLeftIconSVG from '../../../assets/chevron-left.svg';

const Header = () => (
  <S.Layout>
    <S.Box>
      <S.BackwardButton type="button">
        <S.ChevronLeftImage src={ChevronLeftIconSVG} />
      </S.BackwardButton>
    </S.Box>
    <S.Box />
  </S.Layout>
);

export default Header;
