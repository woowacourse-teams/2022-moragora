import {
  MeetingListResponseBody,
  MeetingResponseBody,
} from 'types/meetingType';
import { User } from 'types/userType';
import request from 'utils/request';

export const createMeetingApi = async ({
  accessToken,
  formDataObject,
}: {
  accessToken: User['accessToken'];
  formDataObject: Record<string, any>;
}) => {
  if (!accessToken) {
    throw new Error('미팅을 생성하는 중 에러가 발생했습니다.');
  }

  const meetingCreateResponse = await request('/meetings', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify(formDataObject),
  });

  const location = meetingCreateResponse.headers.get('location') as string;

  return request<{ id: number }>(location, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};

export const getMeetingData =
  (id: string | undefined, accessToken: User['accessToken']) => async () => {
    if (!id || !accessToken) {
      throw new Error('미팅 정보 요청 중 에러가 발생했습니다.');
    }

    return request<MeetingResponseBody>(`/meetings/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    });
  };

export const getMeetingListApi =
  (accessToken: User['accessToken']) => async () => {
    if (!accessToken) {
      throw new Error('미팅 목록 요청 중 에러가 발생했습니다.');
    }

    return request<MeetingListResponseBody>('/meetings/me', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    });
  };
