import { css } from '@emotion/react';
import React from 'react';
import * as S from './UserItem.styled';
import CoffeeIconSVG from '../../assets/coffee.svg';
import Checkbox from '../../components/@shared/Checkbox';

type User = {
  id: number;
  email: string;
  password: string;
  nickname: string;
  accessToken: null | string;
  tardyCount: number;
  attendanceStatus: string;
};

type UserItemProps = {
  user: Omit<User, 'accessToken'>;
  handleChecked: (user: Omit<User, 'accessToken'>, checked: boolean) => void;
};

const UserItem: React.FC<UserItemProps> = ({ user, handleChecked }) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    handleChecked(user, e.target.checked);
  };

  return (
    <S.Layout>
      <S.Box>
        <span>{user.nickname}</span>
        <S.CoffeeIconImageBox>
          {Array.from({ length: user.tardyCount }).map((_, index) => (
            <S.CoffeeIconImage src={CoffeeIconSVG} key={index} />
          ))}
        </S.CoffeeIconImageBox>
      </S.Box>
      <Checkbox
        onChange={handleChange}
        checked={user.attendanceStatus === 'tardy' ? false : true}
      />
    </S.Layout>
  );
};

export default UserItem;
