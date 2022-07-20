import * as S from './MeetingListPage.styled';
import Button from 'components/@shared/Button';
import Footer from 'components/layouts/Footer';
import MeetingItem from 'components/MeetingItem';
import CoffeeStackItem from 'components/CoffeeStackItem';
import Spinner from 'components/@shared/Spinner';
import { MeetingListResponseBody } from 'types/meetingType';
import useFetch from 'hooks/useFetch';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';

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
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
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
            {meetingListState.meetings.length > 0 ? (
              meetingListState.meetings.map((meeting) => (
                <li key={meeting.id}>
                  <MeetingItem meeting={meeting} />
                </li>
              ))
            ) : (
              <div>참여 중인 모임이 없습니다.</div>
            )}
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
      <Footer>
        <Button form="attendance-form" type="submit">
          추가하기
        </Button>
      </Footer>
    </>
  );
};

export default MeetingListPage;
