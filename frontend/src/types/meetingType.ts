import { Participant } from './userType';

export type Meeting = {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  entranceTime: string;
  closingTime: string;
  active: boolean;
  userIds: number[];
  leaveTime: string;
  attendanceCount: number;
};

export type MeetingItem = Omit<
  Meeting,
  'leaveTime' | 'attendanceCount' | 'userIds'
>;

export type MeetingListResponseBody = {
  serverTime: string;
  meetings: MeetingItem[];
};

export type MeetingResponseBody = Omit<
  Meeting,
  'userIds' | 'closingTime' | 'active'
> & {
  users: Participant[];
  attendanceCount: number;
};
