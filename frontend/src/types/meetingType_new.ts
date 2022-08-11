import { MeetingEvent } from './eventType_new';
import { Participant } from './userType_new';

export type Meeting = {
  id: number;
  name: string;
  isActive: boolean;
  userIds: number[];
  attendanceEventCount: number;
};

export type MeetingWithTardyCount = {
  id: number;
  name: string;
  isActive: boolean;
  tardyCount: number;
};

export type MeetingCreateRequestBody = {
  name: string;
  userIds: number[];
};

export type MeetingListResponseBody = {
  meetings: {
    id: number;
    name: string;
    isActive: boolean;
    tardyCount: number;
    isLoginUserMaster: boolean;
    isCoffeeTime: boolean;
    upcomingEvent: MeetingEvent | null;
  }[];
};

export type MeetingResponseBody = {
  id: number;
  name: string;
  attendanceEventCount: number;
  isActive: boolean;
  isLoginUserMaster: boolean;
  isCoffeeTime: boolean;
  users: (Participant & { isMaster: boolean })[];
};
