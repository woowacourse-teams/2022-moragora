import React from 'react';
import AvatarIconSVG from '../../../assets/avatar.svg';
import * as S from './Avatar.styled';

const Avatar = () => {
  return (
    <S.Layout>
      <S.Image src={AvatarIconSVG} alt="avatar" />
    </S.Layout>
  );
};

export default Avatar;
