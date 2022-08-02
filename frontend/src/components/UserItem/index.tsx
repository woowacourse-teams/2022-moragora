import React, { useContext, useState } from 'react';
import * as S from './UserItem.styled';
import Checkbox from 'components/@shared/Checkbox';
import CoffeeIconSVG from 'assets/coffee.svg';
import { Participant } from 'types/userType';
import { userContext, UserContextValues } from 'contexts/userContext';
import useMutation from 'hooks/useMutation';
import { putUserAttendanceApi } from 'apis/userApis';
import { ATTENDANCE_STATUS } from 'consts';

type UserItemProps = {
  user: Participant;
  meetingId: string;
  disabled: boolean;
};

const UserItem: React.FC<UserItemProps> = ({ user, meetingId, disabled }) => {
  const { accessToken } = useContext(userContext) as UserContextValues;
  const [checked, setChecked] = useState<boolean>(
    ATTENDANCE_STATUS[user.attendanceStatus]
  );

  const { isLoading: attendanceMutateLoading, mutate: attendanceMutate } =
    useMutation(putUserAttendanceApi, {
      onMutate: () => {
        setChecked(!checked);
      },
      onError: () => {
        setChecked(!checked);
        alert('출석체크 중 오류가 발생했습니다.');
      },
    });

  const handleChange = async ({
    target: { checked },
  }: React.ChangeEvent<HTMLInputElement>) => {
    if (!attendanceMutateLoading) {
      attendanceMutate({
        meetingId,
        userId: user.id,
        accessToken,
        AttendanceStatus: checked ? 'present' : 'tardy',
      });
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
      <Checkbox onChange={handleChange} checked={checked} disabled={disabled} />
    </S.Layout>
  );
};

export default UserItem;
