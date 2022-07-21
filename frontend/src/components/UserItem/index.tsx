import React, { useContext, useState } from 'react';
import * as S from './UserItem.styled';
import Checkbox from 'components/@shared/Checkbox';
import CoffeeIconSVG from 'assets/coffee.svg';
import { Participant, AttendanceStatus, User } from 'types/userType';
import { TOKEN_ERROR_STATUS_CODES } from 'consts';
import { userContext } from 'contexts/userContext';

type UserItemProps = {
  user: Participant;
  meetingId: string;
};

const ATTENDANCE_STATUS: Record<AttendanceStatus, boolean> = {
  tardy: false,
  present: true,
} as const;

const userAttendanceFetch = (
  url: string,
  payload: any,
  accessToken?: User['accessToken']
) => {
  return fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify(payload),
  });
};

const UserItem: React.FC<UserItemProps> = ({ user, meetingId }) => {
  const userState = useContext(userContext);
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
        },
        userState?.user?.accessToken
      );

      if (!response.ok) {
        if (TOKEN_ERROR_STATUS_CODES.includes(response.status)) {
        }

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
