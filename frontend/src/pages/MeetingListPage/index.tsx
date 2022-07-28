import { useEffect } from 'react';
import { css } from '@emotion/react';
import * as S from './MeetingListPage.styled';
import Footer from 'components/layouts/Footer';
import MeetingItem from 'components/MeetingItem';
import MeetingItemSkeleton from 'components/MeetingItemSkeleton';
import CoffeeStackItem from 'components/CoffeeStackItem';
import CoffeeStackItemSkeleton from 'components/CoffeeStackItemSkeleton';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';
import useFetch from 'hooks/useFetch';
import useTimer from 'hooks/useTimer';
import NoSearchResultIconSVG from 'assets/NoSearchResult.svg';
import { MeetingListResponseBody } from 'types/meetingType';

const MeetingListPage = () => {
  const {
    data: meetingListState,
    loading,
    error,
    refetch,
  } = useFetch<MeetingListResponseBody>('/meetings/me');
  const { currentTimestamp, elapsed } = useTimer(
    meetingListState?.serverTime || Date.now()
  );
  const currentDate = new Date(currentTimestamp);
  const activeMeetings = meetingListState?.meetings.filter(
    ({ isActive }) => isActive
  );
  const inactiveMeetings = meetingListState?.meetings.filter(
    ({ isActive }) => !isActive
  );
  const sortedMeetings = [
    ...(activeMeetings || []),
    ...(inactiveMeetings || []),
  ];
  const currentLocaleTimeString = currentDate.toLocaleTimeString(undefined, {
    hourCycle: 'h24',
    hour: '2-digit',
    minute: '2-digit',
  });

  useEffect(() => {
    const closingMeeting = sortedMeetings.find(
      (meeting) =>
        meeting.isActive && meeting.closingTime === currentLocaleTimeString
    );

    if (closingMeeting) {
      refetch();
    }
  }, [currentLocaleTimeString]);

  if (loading && !meetingListState) {
    return (
      <>
        <S.Layout>
          <S.TimeSection>
            <S.DateBox>
              <S.Title>Today</S.Title>
              <S.EmptyStateDateParagraph />
            </S.DateBox>
            <S.DateBox>
              <S.Title>Time</S.Title>
              <S.EmptyStateTimeParagraph />
            </S.DateBox>
          </S.TimeSection>
          <S.MeetingListSection>
            <S.TitleBox>
              <S.Title>참여 중인 모임</S.Title>
            </S.TitleBox>
            <S.MeetingList
              css={css`
                overflow: hidden;
              `}
            >
              {Array.from({ length: 2 }).map((_, id) => (
                <li key={id}>
                  <MeetingItemSkeleton />
                </li>
              ))}
            </S.MeetingList>
          </S.MeetingListSection>
          <S.CoffeeStackSection>
            <S.TitleBox>
              <S.Title>커피 스택</S.Title>
            </S.TitleBox>
            <S.CoffeeStackList>
              {Array.from({ length: 3 }).map((_, id) => (
                <li key={id}>
                  <CoffeeStackItemSkeleton />
                </li>
              ))}
            </S.CoffeeStackList>
          </S.CoffeeStackSection>
        </S.Layout>
        <Footer />
      </>
    );
  }

  if (error || !meetingListState) {
    return (
      <>
        <S.Layout>
          <S.ErrorBox>
            <ErrorIcon />
            <ReloadButton
              onClick={() => {
                refetch();
              }}
            />
          </S.ErrorBox>
        </S.Layout>
        <Footer />
      </>
    );
  }

  if (meetingListState.meetings.length === 0) {
    return (
      <>
        <S.Layout>
          <S.EmptyStateBox>
            <S.EmptyStateImage src={NoSearchResultIconSVG} alt="no meetings" />
            <S.EmptyStateTitle>아직 모임이 없어요.</S.EmptyStateTitle>
            <S.EmptyStateParagraph>
              직접 모임을 등록하러 가볼까요?
            </S.EmptyStateParagraph>
            <S.MeetingCreateLink to="/meeting/create">
              모임 생성하기
            </S.MeetingCreateLink>
          </S.EmptyStateBox>
        </S.Layout>
        <Footer />
      </>
    );
  }

  return (
    <>
      <S.Layout>
        <S.TimeSection>
          <S.DateBox>
            <S.Title>Today</S.Title>
            <S.DateParagraph>
              {currentDate.toLocaleDateString(undefined, {
                weekday: 'long',
                month: 'long',
                day: 'numeric',
              })}
            </S.DateParagraph>
          </S.DateBox>
          <S.DateBox>
            <S.Title>Time</S.Title>
            <S.DateParagraph>
              {currentDate.toLocaleTimeString(undefined, {
                hourCycle: 'h24',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
              })}
            </S.DateParagraph>
          </S.DateBox>
        </S.TimeSection>
        <S.MeetingListSection>
          <S.TitleBox>
            <S.Title>참여 중인 모임</S.Title>
            <S.PageLink to="/meeting/create">생성하기</S.PageLink>
          </S.TitleBox>
          <S.MeetingList>
            {sortedMeetings.map((meeting) => (
              <li key={meeting.id}>
                <MeetingItem meeting={meeting} />
              </li>
            ))}
          </S.MeetingList>
        </S.MeetingListSection>
        <S.CoffeeStackSection>
          <S.TitleBox>
            <S.Title>커피 스택</S.Title>
          </S.TitleBox>
          <S.CoffeeStackList>
            {meetingListState.meetings.map((meeting) => (
              <li key={meeting.id}>
                <CoffeeStackItem
                  name={meeting.name}
                  tardyCount={meeting.tardyCount}
                />
              </li>
            ))}
          </S.CoffeeStackList>
        </S.CoffeeStackSection>
      </S.Layout>
      <Footer />
    </>
  );
};

export default MeetingListPage;
