import * as S from './AttendanceSection.styled';
import { getAttendancesApi } from 'apis/userApis';
import React, { useContext } from 'react';
import { userContext, UserContextValues } from 'contexts/userContext';
import useQuery from 'hooks/useQuery';
import { MeetingListResponseBody } from 'types/meetingType';
import Spinner from 'components/@shared/Spinner';
import { css } from '@emotion/react';
import UserItem from 'components/UserItem';
import emptyInboxSVG from 'assets/empty-inbox.svg';

type AttendanceSectionProps = {
  currentMeeting: MeetingListResponseBody['meetings'][0];
};

const AttendanceSection: React.FC<AttendanceSectionProps> = ({
  currentMeeting,
}) => {
  const { accessToken } = useContext(userContext) as UserContextValues;

  const attendancesQuery = useQuery(
    ['attendances', currentMeeting.id],
    getAttendancesApi(accessToken, currentMeeting.id),
    {
      enabled: false,
    }
  );

  if (attendancesQuery.isLoading) {
    return (
      <S.SpinnerBox>
        <Spinner />
      </S.SpinnerBox>
    );
  }

  if (attendancesQuery.isError) {
    return (
      <S.EmptyStateBox>
        <S.EmptyStateImage src={emptyInboxSVG} alt="empty inbox" />
        <S.EmptyStateTitle>오늘은 출결이 없어요.</S.EmptyStateTitle>
        <S.EmptyStateParagraph>다음에 다시 만나요.</S.EmptyStateParagraph>
      </S.EmptyStateBox>
    );
  }

  return (
    <S.Layout>
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
                  !currentMeeting.isActive || !currentMeeting.isLoginUserMaster
                }
              />
            ))}
          </S.UserList>
        </S.UserListBox>
      )}
    </S.Layout>
  );
};

export default AttendanceSection;
