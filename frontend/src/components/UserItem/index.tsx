import { css } from '@emotion/react';
import React from 'react';
import * as S from './UserItem.styled';
import CoffeeIconSVG from '../../assets/coffee.svg';
import Checkbox from '../../components/@shared/Checkbox';

type User = {
  id: 1;
  email: string;
  password: string;
  nickname: string;
  accessToken: null | string;
  tardyCount: number;
};

type UserItemProps = {
  user: Omit<User, 'accessToken'>;
};

const UserItem: React.FC<UserItemProps> = ({ user }) => {
  return (
    <S.Layout key={user.id}>
      <S.Box>
        <span>{user.nickname}</span>
        <S.CoffeeIconImageBox>
          {Array.from({ length: user.tardyCount }).map((_, index) => (
            <S.CoffeeIconImage src={CoffeeIconSVG} key={index} />
          ))}
        </S.CoffeeIconImageBox>
      </S.Box>
      <Checkbox />
    </S.Layout>
  );
};

export default UserItem;
