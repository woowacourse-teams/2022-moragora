import { useContext, useState } from 'react';
import * as S from './GeolocationCheckInPage.styled';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getMeetingListApi } from 'apis/meetingApis';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';
import CheckMeetingItem from 'components/CheckMeetingItem';
import useQuery from 'hooks/useQuery';
import useMutation from 'hooks/useMutation';
import { MeetingListResponseBody } from 'types/meetingType';
import emptyInboxSVG from 'assets/empty-inbox.svg';
import { postUserGeolocationAttendanceApi } from 'apis/userApis';
import useGeolocation from 'hooks/useGeolocation';
import Button from 'components/@shared/Button';

const GeolocationCheckInPage = () => {
  const [currentMeeting, setCurrentMeeting] = useState<
    MeetingListResponseBody['meetings'][0] | null
  >(null);
  const { accessToken, user } = useContext(userContext) as UserContextValues;
  const { currentPosition } = useGeolocation();

  const meetingListQuery = useQuery(
    ['meetingList'],
    getMeetingListApi(accessToken),
    {
      onSuccess: ({ body: { meetings } }) => {
        const activeMeeting = meetings.find((meeting) => meeting.isActive);

        if (activeMeeting) {
          setCurrentMeeting(activeMeeting);
        }
      },
    }
  );

  const geolocationCheckInMutation = useMutation(
    postUserGeolocationAttendanceApi({ accessToken }),
    {
      onSuccess: () => {
        alert('출석에 성공했습니다.');
        meetingListQuery.refetch();
      },
      onError: () => {
        alert('출석을 실패했습니다.');
      },
    }
  );

  const handleMeetingItemClick = (
    meeting: MeetingListResponseBody['meetings'][0]
  ) => {
    setCurrentMeeting(meeting);
  };

  const handleCheckInClick = () => {
    if (currentMeeting && user && currentPosition) {
      geolocationCheckInMutation.mutate({
        meetingId: currentMeeting?.id,
        userId: user.id,
        latitude: currentPosition.coords.latitude,
        longitude: currentPosition.coords.longitude,
      });
    }
  };

  if (meetingListQuery.isLoading) {
    return (
      <S.Layout>
        <S.SpinnerBox>
          <Spinner />
        </S.SpinnerBox>
      </S.Layout>
    );
  }

  if (meetingListQuery.isError) {
    return (
      <S.Layout>
        <S.ErrorBox>
          <ErrorIcon />
          <ReloadButton
            onClick={() => {
              meetingListQuery.refetch();
            }}
          />
        </S.ErrorBox>
      </S.Layout>
    );
  }

  if (!currentMeeting) {
    return (
      <S.Layout>
        <S.EmptyStateBox>
          <S.EmptyStateImage src={emptyInboxSVG} alt="empty inbox" />
          <S.EmptyStateTitle>출석 가능한 모임이 없어요.</S.EmptyStateTitle>
        </S.EmptyStateBox>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.CheckTimeSection>
        <S.SectionTitle>출결 중인 모임</S.SectionTitle>
        <S.MeetingList>
          {meetingListQuery.data?.body.meetings
            .filter((meeting) => meeting.isActive)
            .map((meeting) => (
              <CheckMeetingItem
                key={meeting.id}
                onClick={handleMeetingItemClick}
                meeting={meeting}
                clicked={meeting.id === currentMeeting?.id}
              >
                {meeting.name}
              </CheckMeetingItem>
            ))}
        </S.MeetingList>
      </S.CheckTimeSection>
      <S.ButtonBox>
        <Button type="button" onClick={handleCheckInClick}>
          출석하기
        </Button>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default GeolocationCheckInPage;
