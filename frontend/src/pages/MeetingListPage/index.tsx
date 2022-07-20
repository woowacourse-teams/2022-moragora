import { css } from '@emotion/react';
import * as S from './MeetingListPage.styled';
import Footer from 'components/layouts/Footer';
import MeetingItem from 'components/MeetingItem';
import MeetingItemSkeleton from 'components/MeetingItemSkeleton';
import CoffeeStackItem from 'components/CoffeeStackItem';
import CoffeeStackItemSkeleton from 'components/CoffeeStackItemSkeleton';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';
import DivideLine from 'components/@shared/DivideLine';
import useFetch from 'hooks/useFetch';
import NoSearchResultIconSVG from 'assets/NoSearchResult.svg';
import { MeetingListResponseBody } from 'types/meetingType';

const MeetingListPage = () => {
  const {
    data: meetingListState,
    loading,
    error,
    refetch,
  } = useFetch<MeetingListResponseBody>('/meetings/me');

  if (loading) {
    return (
      <>
        <S.Layout>
          <S.TimeSection>
            <S.DateBox>
              <S.Title>Today</S.Title>
              <S.DateParagraph>
                <div
                  css={css`
                    margin: 0.35rem;
                    border-radius: 0.5rem;
                    height: 1rem;
                    width: 10rem;
                    animation: skeleton-gradient 1.5s infinite ease-in-out;
                  `}
                />
              </S.DateParagraph>
            </S.DateBox>
            <S.DateBox>
              <S.Title>Time</S.Title>
              <div
                css={css`
                  margin: 0.35rem;
                  border-radius: 0.5rem;
                  height: 1rem;
                  width: 6rem;
                  animation: skeleton-gradient 1.5s infinite ease-in-out;
                `}
              />
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

  const activeMeetings = meetingListState.meetings.filter(
    ({ active }) => active
  );
  const inactiveMeetings = meetingListState.meetings.filter(
    ({ active }) => !active
  );
  const sortedMeetings = [...activeMeetings, ...inactiveMeetings];

  return (
    <>
      <S.Layout>
        <S.TimeSection>
          <S.DateBox>
            <S.Title>Today</S.Title>
            <S.DateParagraph>
              {new Date().toLocaleDateString(undefined, {
                weekday: 'long',
                month: 'long',
                day: 'numeric',
              })}
            </S.DateParagraph>
          </S.DateBox>
          <S.DateBox>
            <S.Title>Time</S.Title>
            <S.DateParagraph>{meetingListState.serverTime}</S.DateParagraph>
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
