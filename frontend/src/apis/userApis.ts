import {
  GetLoginUserDataResponseBody,
  Participant,
  User,
  UserCoffeeStatsResponseBody,
  UserLoginRequestBody,
  UserLoginResponseBody,
  UserRegisterRequestBody,
  UserUpdateNicknameRequestBody,
} from 'types/userType';
import request from '../utils/request';

type PutUserAttendanceApiParameter = {
  meetingId: string;
  userId: Participant['id'];
  accessToken: User['accessToken'];
  attendanceStatus: Participant['attendanceStatus'];
};

export const checkEmailApi = (email: User['email']) => () =>
  request<{ isExist: boolean }>(`/users/check-email?email=${email}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

export const submitRegisterApi = async (payload: UserRegisterRequestBody) => {
  await request<{}>('/users', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  const { nickname, ...loginRequestBody } = payload;

  return submitLoginApi(loginRequestBody);
};

export const submitLoginApi = async (payload: UserLoginRequestBody) => {
  const loginResponse = await request<UserLoginResponseBody>('/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  if (!loginResponse.body.accessToken) {
    throw new Error('로그인 중 오류가 발생했습니다.');
  }

  const accessToken = loginResponse.body.accessToken;
  const loginUserResponse = await request<GetLoginUserDataResponseBody>(
    '/users/me',
    {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    }
  );

  return { ...loginUserResponse, accessToken };
};

export const putUserAttendanceApi = async ({
  meetingId,
  userId,
  accessToken,
  attendanceStatus,
}: PutUserAttendanceApiParameter) => {
  if (!accessToken) {
    throw new Error('미팅 정보를 불러오는 중 에러가 발생했습니다.');
  }

  return request<{}>(`/meetings/${meetingId}/users/${userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify({ attendanceStatus }),
  });
};

export const getLoginUserDataApi =
  (accessToken: User['accessToken']) => async () => {
    if (!accessToken) {
      throw new Error('내 정보를 가져오는 중 에러가 발생했습니다.');
    }

    return request<GetLoginUserDataResponseBody>('/users/me', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    });
  };

export const getUserCoffeeStatsApi =
  (id: string | undefined, accessToken: User['accessToken']) => async () => {
    if (!accessToken) {
      throw new Error('유저별 커피정보를 불러오는 중 에러가 발생했습니다.');
    }

    return request<UserCoffeeStatsResponseBody>(`/meetings/${id}/coffees/use`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    });
  };

export const updateNicknameApi =
  (accessToken: User['accessToken']) =>
  async (payload: UserUpdateNicknameRequestBody) => {
    if (!accessToken) {
      throw new Error('닉네임 변경 중 에러가 발생했습니다.');
    }

    return request(`/users/me/nickname`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(payload),
    });
  };
