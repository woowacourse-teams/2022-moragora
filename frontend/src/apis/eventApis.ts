import {
  EventCreateRequestBody,
  DeleteEventsRequestBody,
  MeetingEvent,
} from 'types/eventType';
import { privateRequest } from './api';

export const getEventsApi = (meetingId: string) => () =>
  privateRequest.get<{ events: MeetingEvent[] }>(
    `/meetings/${meetingId}/events`
  );

export const createEventsApi =
  (meetingId: string) => (payload: EventCreateRequestBody) =>
    privateRequest.post<{}>(`/meetings/${meetingId}/events`, payload);

export const deleteEventsApi =
  (meetingId: string) => (payload: DeleteEventsRequestBody) =>
    privateRequest.delete<{}>(`/meetings/${meetingId}/events`, {
      data: payload,
    });

export const getUpcomingEventApi = (meetingId: string) => () =>
  privateRequest.get<MeetingEvent>(`/meetings/${meetingId}/events/upcoming`);
