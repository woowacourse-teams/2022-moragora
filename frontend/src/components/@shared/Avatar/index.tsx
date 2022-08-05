import React from 'react';
import * as S from './Avatar.styled';
import AvatarIconSVG from 'assets/avatar.svg';

const Avatar = () => {
  return (
    <S.Layout>
      <S.Image src={AvatarIconSVG} alt="avatar" />
    </S.Layout>
  );
};

export default Avatar;
