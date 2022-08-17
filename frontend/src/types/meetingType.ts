import { MeetingEvent } from './eventType';
import { Participant, User } from './userType';

export type Meeting = {
  id: number;
  name: string;
  isActive: boolean;
  userIds: number[];
  attendedEventCount: number;
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

export type MeetingUpdateRequestBody = {
  name: string;
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
  attendedEventCount: number;
  isActive: boolean;
  isLoginUserMaster: boolean;
  isCoffeeTime: boolean;
  users: (Participant & { isMaster: boolean })[];
};

export type MeetingMasterAssignRequestBody = {
  userId: User['id'];
};

export type MeetingNameUpdateRequestBody = {
  name: Meeting['name'];
};
