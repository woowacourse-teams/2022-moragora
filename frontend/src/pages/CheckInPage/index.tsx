import { useContext, useEffect, useState } from 'react';
import * as S from './CheckInPage.styled';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getMeetingListApi } from 'apis/meetingApis';
import Footer from 'components/layouts/Footer';
import UserItem from 'components/UserItem';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';
import useQuery from 'hooks/useQuery';
import { MeetingListResponseBody } from 'types/meetingType';
import CheckMeetingItem from 'components/CheckMeetingItem/indext';
import { getAttendancesApi } from 'apis/userApis';
import { css } from '@emotion/react';

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

  const attendancesQuery = useQuery(
    ['attendances'],
    getAttendancesApi(currentMeeting?.id, accessToken),
    {
      enabled: !!currentMeeting,
    }
  );

  const handleMeetingItemClick = (
    meeting: MeetingListResponseBody['meetings'][0]
  ) => {
    setCurrentMeeting(meeting);
    attendancesQuery.refetch();
  };

  if (meetingListQuery.isLoading) {
    return (
      <>
        <S.Layout>
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
        </S.Layout>
        <Footer />
      </>
    );
  }

  if (
    meetingListQuery.isError ||
    attendancesQuery.isError ||
    !meetingListQuery.data
  ) {
    return (
      <>
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
        <Footer />
      </>
    );
  }

  if (!currentMeeting) {
    return (
      <>
        <S.Layout>
          <S.EndedCheckTimeSection>
            <S.SectionTitle>출결이 끝난 모임</S.SectionTitle>
            <S.MeetingList>
              {meetingListQuery.data.body.meetings
                .filter((meeting) => !meeting.isActive)
                .map((meeting) => (
                  <CheckMeetingItem
                    key={meeting.id}
                    onClick={handleMeetingItemClick}
                    meeting={meeting}
                    clicked={false}
                  >
                    {meeting.name}
                  </CheckMeetingItem>
                ))}
            </S.MeetingList>
          </S.EndedCheckTimeSection>
          <S.CheckTimeSection>
            <S.SectionTitle>출결 중인 모임</S.SectionTitle>
            <S.MeetingList>
              {meetingListQuery.data.body.meetings
                .filter((meeting) => meeting.isActive)
                .map((meeting) => (
                  <CheckMeetingItem
                    key={meeting.id}
                    onClick={handleMeetingItemClick}
                    meeting={meeting}
                    clicked={false}
                  >
                    {meeting.name}
                  </CheckMeetingItem>
                ))}
            </S.MeetingList>
          </S.CheckTimeSection>
          <S.UserListSection>
            <S.SectionTitle>출결</S.SectionTitle>
            <div>미팅이 없습니다.</div>
          </S.UserListSection>
        </S.Layout>
        <Footer />
      </>
    );
  }

  return (
    <>
      <S.Layout>
        <S.EndedCheckTimeSection>
          <S.SectionTitle>출결이 끝난 모임</S.SectionTitle>
          <S.MeetingList>
            {meetingListQuery.data.body.meetings
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
            {meetingListQuery.data.body.meetings
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
        <S.UserListSection>
          <S.SectionTitle>출결</S.SectionTitle>
          {attendancesQuery.isLoading ? (
            <div
              css={css`
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
              `}
            >
              <Spinner />
            </div>
          ) : (
            <S.UserListBox>
              <S.UserList>
                {attendancesQuery.data?.body.users.map((user) => (
                  <UserItem
                    key={user.id}
                    meetingId={currentMeeting.id}
                    user={user}
                    disabled={
                      !currentMeeting.isActive ||
                      !currentMeeting.isLoginUserMaster
                    }
                  />
                ))}
              </S.UserList>
            </S.UserListBox>
          )}
        </S.UserListSection>
      </S.Layout>
      <Footer />
    </>
  );
};

export default CheckInPage;
