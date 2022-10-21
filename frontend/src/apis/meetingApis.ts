import {
  MeetingListResponseBody,
  MeetingMasterAssignRequestBody,
  MeetingNameUpdateRequestBody,
  MeetingResponseBody,
} from 'types/meetingType';
import request from 'utils/request';

export const createMeetingApi = async (formDataObject: Record<string, any>) => {
  const meetingCreateResponse = await request('/meetings', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(formDataObject),
  });

  const location = meetingCreateResponse.headers.get('location') as string;

  return request<{ id: number }>(location, {
    headers: {},
  });
};

export const getMeetingData = (id: string | undefined) => async () => {
  return request<MeetingResponseBody>(`/meetings/${id}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

export const getMeetingListApi = () => async () => {
  return request<MeetingListResponseBody>('/meetings/me', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

export const postEmptyCoffeeStackApi = ({ id }: { id: string }) => {
  return request<{}>(`/meetings/${id}/coffees/use`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

export const updateMeetingNameApi =
  (meetingId: string) => (payload: MeetingNameUpdateRequestBody) =>
    request(`/meetings/${meetingId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

export const assignMasterApi =
  (meetingId: number) => (payload: MeetingMasterAssignRequestBody) =>
    request(`/meetings/${meetingId}/master`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

export const deleteMeetingApi = (meetingId: string) => () =>
  request<{}>(`/meetings/${meetingId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
  });

export const leaveMeetingApi = (meetingId: string) => () =>
  request<{}>(`/meetings/${meetingId}/me`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
  });
