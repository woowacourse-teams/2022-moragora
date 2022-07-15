import React from 'react';
import { css } from '@emotion/react';
import * as S from './Header.styled';
import Logo from '../../Logo';
import ChevronLeftIconSVG from '../../../assets/chevron-left.svg';
import { Link, useNavigate } from 'react-router-dom';

const Header = () => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(-1);
  };

  return (
    <S.Layout>
      <S.Box>
        <S.BackwardButton type="button" onClick={handleClick}>
          <S.ChevronLeftImage src={ChevronLeftIconSVG} />
        </S.BackwardButton>
      </S.Box>
      <S.Box />
    </S.Layout>
  );
};

export default Header;
