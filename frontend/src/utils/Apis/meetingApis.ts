import { UserContextValues } from 'contexts/userContext';
import {
  MeetingListResponseBody,
  MeetingResponseBody,
} from 'types/meetingType';
import request from 'utils/request';

export const createMeetingApi = async ({
  accessToken,
  formDataObject,
}: {
  accessToken: string;
  formDataObject: Record<string, any>;
}) => {
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
  (id: string, user: UserContextValues['user']) => async () => {
    if (!user) {
      throw new Error('유저가 존재하지 않습니다.');
    }

    if (!user.accessToken) {
      throw new Error('미팅 정보를 불러오는 중 에러가 발생했습니다.');
    }

    return request<MeetingResponseBody>(`/meetings/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user.accessToken}`,
      },
    });
  };

export const getMeetingListApi =
  (user: UserContextValues['user']) => async () => {
    if (!user) {
      throw new Error('유저가 존재하지 않습니다.');
    }

    const { accessToken } = user;

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
