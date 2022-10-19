import React, { useContext, useState } from 'react';
import * as S from './UserItem.styled';
import Checkbox from 'components/@shared/Checkbox';
import { userContext, UserContextValues } from 'contexts/userContext';
import useMutation from 'hooks/useMutation';
import { postUserAttendanceApi } from 'apis/userApis';
import { ATTENDANCE_STATUS } from 'consts';
import { Attendance } from 'types/attendanceType';

type UserItemProps = {
  user: Attendance;
  meetingId: number;
  disabled: boolean;
};

const UserItem: React.FC<UserItemProps> = ({ user, meetingId, disabled }) => {
  const userState = useContext(userContext) as UserContextValues;
  const [checked, setChecked] = useState<boolean>(
    ATTENDANCE_STATUS[user.attendanceStatus]
  );

  const attendanceMutation = useMutation(postUserAttendanceApi, {
    onMutate: () => {
      setChecked((prev) => !prev);
    },
    onError: () => {
      setChecked((prev) => !prev);
      alert('출석체크 중 오류가 발생했습니다.');
    },
  });

  const handleChange = async ({
    target: { checked },
  }: React.ChangeEvent<HTMLInputElement>) => {
    if (!attendanceMutation.isLoading) {
      attendanceMutation.mutate({
        meetingId,
        userId: user.id,
        accessToken: userState.accessToken,
        isPresent: checked,
      });
    }
  };

  return (
    <S.Layout>
      <S.Box>
        <span>{user.nickname}</span>
      </S.Box>
      {user.attendanceStatus === 'tardy' ? (
        <S.AttendanceStatusText>지각</S.AttendanceStatusText>
      ) : (
        <Checkbox
          onChange={handleChange}
          checked={checked}
          disabled={disabled}
        />
      )}
    </S.Layout>
  );
};

export default UserItem;
