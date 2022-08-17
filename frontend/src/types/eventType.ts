export type MeetingEvent = {
  id: number;
  date: string;
  attendanceOpenTime: string;
  attendanceClosedTime: string;
  meetingStartTime: string;
  meetingEndTime: string;
};

export type EventResposeBody = MeetingEvent;

export type EventListResponseBody = MeetingEvent[];
