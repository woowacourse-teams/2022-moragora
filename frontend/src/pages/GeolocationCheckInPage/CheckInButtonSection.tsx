import React, { useContext } from 'react';
import Button from 'components/@shared/Button';
import useQuery from 'hooks/useQuery';
import {
  getAttendancesApi,
  postUserGeolocationAttendanceApi,
} from 'apis/userApis';
import { userContext, UserContextValues } from 'contexts/userContext';
import { MeetingListResponseBody } from 'types/meetingType';
import useMutation from 'hooks/useMutation';
import * as S from './CheckInButtonSection.styled';

type CheckInButtonSectionProps = React.HTMLAttributes<HTMLDivElement> & {
  meeting: MeetingListResponseBody['meetings'][0];
  currentPosition?: GeolocationPosition;
  refetchMeetingList: () => void;
};

const CheckInButtonSection = ({
  currentPosition,
  meeting,
  refetchMeetingList,
  ...props
}: CheckInButtonSectionProps) => {
  const { user } = useContext(userContext) as UserContextValues;
  const attendancesQuery = useQuery(
    ['attendances', meeting.id],
    getAttendancesApi(meeting.id),
    {
      enabled: !!meeting,
    }
  );

  const geolocationCheckInMutation = useMutation(
    postUserGeolocationAttendanceApi(),
    {
      onSuccess: () => {
        alert('출석에 성공했습니다.');
        refetchMeetingList();
      },
      onError: () => {
        alert('출석을 실패했습니다.');
        refetchMeetingList();
      },
    }
  );

  const userAttendanceStatus = attendancesQuery.data?.body.users.find(
    ({ id }) => id === user?.id
  )?.attendanceStatus;
  const isCheckInable = !!currentPosition && userAttendanceStatus === 'none';

  const handleCheckInClick = () => {
    if (user && currentPosition) {
      geolocationCheckInMutation.mutate({
        meetingId: meeting.id,
        userId: user.id,
        latitude: currentPosition.coords.latitude,
        longitude: currentPosition.coords.longitude,
      });
    }
  };

  return (
    <S.Layout {...props}>
      <Button
        type="button"
        onClick={handleCheckInClick}
        disabled={!isCheckInable}
      >
        출석하기
      </Button>
    </S.Layout>
  );
};

export default CheckInButtonSection;
