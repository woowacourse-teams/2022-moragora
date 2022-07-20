import * as S from './MeetingListPage.styled';
import Button from 'components/@shared/Button';
import Footer from 'components/layouts/Footer';
import MeetingItem from 'components/MeetingItem';
import CoffeeStackItem from 'components/CoffeeStackItem';
import { MeetingListResponseBody } from 'types/meetingType';

const MeetingListPage = () => {
  return (
    <>
      <S.Layout>
        <S.TimeSection>
          <S.DateBox>
            <S.Title>Today</S.Title>
            <S.DateParagraph>6월 23일 월</S.DateParagraph>
          </S.DateBox>
          <S.DateBox>
            <S.Title>Time</S.Title>
            <S.DateParagraph>AM 10:01</S.DateParagraph>
          </S.DateBox>
        </S.TimeSection>
        <S.MeetingListSection>
          <S.TitleBox>
            <S.Title>참여 중인 모임</S.Title>
            <S.PageLink to="/meeting/create">생성하기</S.PageLink>
          </S.TitleBox>
          <S.MeetingList>
            {/* {meetings.map((meeting) => (
              <li key={meeting.id}>
                <MeetingItem meeting={meeting} />
              </li>
            ))} */}
          </S.MeetingList>
        </S.MeetingListSection>
        <S.CoffeeStackSection>
          <S.TitleBox>
            <S.Title>커피 스택</S.Title>
          </S.TitleBox>
          <S.CoffeeStackList>
            {/* {meetings.map((meeting) => (
              <li key={meeting.id}>
                <CoffeeStackItem name={meeting.name} />
              </li>
            ))} */}
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
