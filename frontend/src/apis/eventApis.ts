import request from 'utils/request';
import {
  EventCreateRequestBody,
  DeleteEventsRequestBody,
  MeetingEvent,
} from 'types/eventType';

export const getEventsApi = (meetingId: string) => () =>
  request<{ events: MeetingEvent[] }>(`/meetings/${meetingId}/events`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

export const createEventsApi =
  (meetingId: string) => (payload: EventCreateRequestBody) =>
    request<{}>(`/meetings/${meetingId}/events`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

export const deleteEventsApi =
  (meetingId: string) => (payload: DeleteEventsRequestBody) =>
    request<{}>(`/meetings/${meetingId}/events`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

export const getUpcomingEventApi = (meetingId: string) => () =>
  request<MeetingEvent>(`/meetings/${meetingId}/events/upcoming`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
