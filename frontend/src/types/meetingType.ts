import { Participant } from './userType';

export type Meeting = {
  id: number;
  name: string;
  isActive: boolean;
  userIds: number[];
  attendanceCount: number;
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
    entranceTime: string;
    closingTime: string;
    tardyCount: number;
    isMaster: boolean;
    isCoffeeTime: boolean;
    hasUpcomingEvent: boolean;
  }[];
};

export type MeetingResponseBody = {
  id: number;
  name: string;
  attendanceCount: number;
  entranceTime: string;
  leaveTime: string;
  isMaster: boolean;
  isCoffeeTime: boolean;
  hasUpcomingEvent: boolean;
  users: Participant[];
};
