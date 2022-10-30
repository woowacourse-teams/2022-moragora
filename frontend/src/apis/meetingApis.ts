import {
  MeetingListResponseBody,
  MeetingMasterAssignRequestBody,
  MeetingNameUpdateRequestBody,
  MeetingResponseBody,
} from 'types/meetingType';
import { privateRequest } from './api';

export const createMeetingApi = async (formDataObject: Record<string, any>) => {
  const meetingCreateResponse = await privateRequest.post(
    '/meetings',
    formDataObject
  );

  const location = meetingCreateResponse.headers.get('location') as string;

  return privateRequest.get<{ id: number }>(location, {
    headers: {},
  });
};

export const getMeetingData = (id: string | undefined) => async () => {
  return privateRequest.get<MeetingResponseBody>(`/meetings/${id}`);
};

export const getMeetingListApi = () => async () => {
  return privateRequest.get<MeetingListResponseBody>('/meetings/me');
};

export const postEmptyCoffeeStackApi = ({ id }: { id: string }) => {
  return privateRequest.post<{}>(`/meetings/${id}/coffees/use`);
};

export const updateMeetingNameApi =
  (meetingId: string) => (payload: MeetingNameUpdateRequestBody) =>
    privateRequest.put(`/meetings/${meetingId}`, payload);

export const assignMasterApi =
  (meetingId: number) => (payload: MeetingMasterAssignRequestBody) =>
    privateRequest.put(`/meetings/${meetingId}/master`, payload);

export const deleteMeetingApi = (meetingId: string) => () =>
  privateRequest.delete<{}>(`/meetings/${meetingId}`);

export const leaveMeetingApi = (meetingId: string) => () =>
  privateRequest.delete<{}>(`/meetings/${meetingId}/me`);
