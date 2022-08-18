import { User } from './userType';

export type Attendance = {
  id: number;
  nickname: string;
  attendanceStatus: 'none' | 'present' | 'tardy';
};

export type AttendancesResponseBody = {
  users: Attendance[];
};

export type PostUserAttendanceRequestBody = {
  meetingId: number;
  userId: User['id'];
  accessToken: User['accessToken'];
  isPresent: boolean;
};
