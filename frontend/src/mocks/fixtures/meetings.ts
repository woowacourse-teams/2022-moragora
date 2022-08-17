import { Meeting } from 'types/meetingType';
import { User } from 'types/userType';

const meetings: (Meeting & { masterId: User['id'] })[] = [
  {
    id: 1,
    name: '리액트 사랑방',
    isActive: false,
    userIds: [1, 2, 3, 4, 5],
    attendanceEventCount: 10,
    masterId: 1,
  },
  {
    id: 2,
    name: '우테코 레벨3 반상회',
    isActive: false,
    userIds: [1, 2, 3, 4, 5, 6],
    attendanceEventCount: 10,
    masterId: 2,
  },
  {
    id: 3,
    name: '이게모임',
    isActive: false,
    userIds: [1, 3, 4, 5, 6, 7],
    attendanceEventCount: 10,
    masterId: 3,
  },
  {
    id: 4,
    name: '2022 우아한테크코스 백엔드4기 비장의 토비의 스프링 스터디',
    isActive: false,
    userIds: [1, 4, 5, 6, 7, 8],
    attendanceEventCount: 10,
    masterId: 4,
  },
  {
    id: 5,
    name: '저건모임',
    isActive: false,
    userIds: [1, 5, 6, 7, 8, 9],
    attendanceEventCount: 10,
    masterId: 5,
  },
  {
    id: 6,
    name: '모임6',
    isActive: false,
    userIds: [1, 6, 7, 8, 9, 10],
    attendanceEventCount: 10,
    masterId: 1,
  },
  {
    id: 7,
    name: '모임7',
    isActive: false,
    userIds: [1, 7, 8, 9, 10, 11],
    attendanceEventCount: 10,
    masterId: 2,
  },
  {
    id: 8,
    name: '사단법인 싱겁게먹기실천연구회',
    isActive: false,
    userIds: [1, 8, 9, 10, 11, 12],
    attendanceEventCount: 10,
    masterId: 3,
  },
];

export default meetings;
