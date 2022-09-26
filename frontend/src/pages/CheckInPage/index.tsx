import { useContext, useState } from 'react';
import * as S from './CheckInPage.styled';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getMeetingListApi } from 'apis/meetingApis';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';
import CheckMeetingItem from 'components/CheckMeetingItem';
import useQuery from 'hooks/useQuery';
import { MeetingListResponseBody } from 'types/meetingType';
import emptyInboxSVG from 'assets/empty-inbox.svg';
import AttendanceSection from './AttendanceSection';

const CheckInPage = () => {
  const [currentMeeting, setCurrentMeeting] =
    useState<MeetingListResponseBody['meetings'][0]>();
  const { accessToken } = useContext(userContext) as UserContextValues;

  const meetingListQuery = useQuery(
    ['meetingList'],
    getMeetingListApi(accessToken),
    {
      onSuccess: ({ body: { meetings } }) => {
        const activeMeeting = meetings.find((meeting) => meeting.isActive);

        setCurrentMeeting(activeMeeting ? activeMeeting : meetings[0]);
      },
    }
  );

  const handleMeetingItemClick = (
    meeting: MeetingListResponseBody['meetings'][0]
  ) => {
    setCurrentMeeting(meeting);
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
          <S.EmptyStateTitle>출석할 모임이 없어요.</S.EmptyStateTitle>
          <S.EmptyStateParagraph>
            모임에 먼저 참여해주세요!
          </S.EmptyStateParagraph>
        </S.EmptyStateBox>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.EndedCheckTimeSection>
        <S.SectionTitle>출결이 끝난 모임</S.SectionTitle>
        <S.MeetingList>
          {meetingListQuery.data?.body.meetings
            .filter((meeting) => !meeting.isActive)
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
      </S.EndedCheckTimeSection>
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
      <AttendanceSection currentMeeting={currentMeeting} />
    </S.Layout>
  );
};

export default CheckInPage;
