import { css } from '@emotion/react';
import React from 'react';
import Button from '../../components/@shared/Button';
import Footer from '../../components/layouts/Footer';
import * as S from './MeetingListPage.styled';

const meetings = [
  {
    id: 1,
    name: '모임1',
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
    name: '모임2',
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
    name: '모임3',
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
        <S.Title>참여 중인 모임</S.Title>
        <S.MeetingList>
          {meetings.map((meeting) => (
            <S.MeetingItem key={meeting.id}>
              <S.MeetingIconSVG
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z" />
              </S.MeetingIconSVG>
              <S.MeetingNameSpan>{meeting.name}</S.MeetingNameSpan>
            </S.MeetingItem>
          ))}
        </S.MeetingList>
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
