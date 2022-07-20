import { css } from '@emotion/react';
import React, { useState } from 'react';
import * as S from './UserItem.styled';
import CoffeeIconSVG from '../../assets/coffee.svg';
import Checkbox from '../../components/@shared/Checkbox';
import { Participant, AttendanceStatus } from 'types/userType';

type UserItemProps = {
  user: Participant;
  meetingId: string;
};

const ATTENDANCE_STATUS: Record<AttendanceStatus, boolean> = {
  tardy: false,
  present: true,
} as const;

const userAttendanceFetch = (url: string, payload: any) => {
  return fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const UserItem: React.FC<UserItemProps> = ({ user, meetingId }) => {
  const [checked, setChecked] = useState<boolean>(
    ATTENDANCE_STATUS[user.attendanceStatus]
  );

  const handleChange = async ({
    target: { checked },
  }: React.ChangeEvent<HTMLInputElement>) => {
    setChecked(checked);

    try {
      const response = await userAttendanceFetch(
        `/meetings/${meetingId}/users/${user.id}`,
        {
          attendanceStatus: checked ? 'present' : 'tardy',
        }
      );

      if (!response.ok) {
        setChecked(!checked);
      }
    } catch (error) {
      alert('출석체크 중 오류가 발생했습니다.');
    }
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
      <Checkbox onChange={handleChange} checked={checked} />
    </S.Layout>
  );
};

export default UserItem;
