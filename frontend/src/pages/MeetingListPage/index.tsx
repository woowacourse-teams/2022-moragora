import React from 'react';
import Button from '../../components/@shared/Button';
import Footer from '../../components/layouts/Footer';
import * as S from './MeetingListPage.styled';
import MeetingItem from '../../components/MeetingItem';
import CoffeeStackItem from '../../components/CoffeeStackItem';

const meetings = [
  {
    id: 1,
    name: '데일리 미팅',
    attendanceCount: 0,
    startDate: '2022-01-01',
    endDate: '2022-12-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 2,
    name: 'php 스터디',
    attendanceCount: 0,
    startDate: '2022-06-01',
    endDate: '2022-07-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 3,
    name: '러닝',
    attendanceCount: 0,
    startDate: '2022-04-01',
    endDate: '2022-09-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 4,
    name: '모임4',
    attendanceCount: 0,
    startDate: '2022-01-01',
    endDate: '2022-12-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 5,
    name: '모임5',
    attendanceCount: 0,
    startDate: '2022-06-01',
    endDate: '2022-07-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 6,
    name: '모임6',
    attendanceCount: 0,
    startDate: '2022-04-01',
    endDate: '2022-09-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 7,
    name: '모임7',
    attendanceCount: 0,
    startDate: '2022-01-01',
    endDate: '2022-12-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
  {
    id: 8,
    name: '모임8',
    attendanceCount: 0,
    startDate: '2022-06-01',
    endDate: '2022-07-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
    users: [
      {
        id: 1,
        email: 'gildong@gmail.com',
        nickname: 'KoreanThief',
        tardyCount: 3,
      },
      {
        id: 2,
        email: 'boki@gmail.com',
        nickname: 'boki',
        tardyCount: 2,
      },
    ],
  },
];

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
            {meetings.map((meeting) => (
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
            {meetings.map((meeting) => (
              <li key={meeting.id}>
                <CoffeeStackItem name={meeting.name} />
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
