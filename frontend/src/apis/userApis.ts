import {
  User,
  GetLoginUserDataResponseBody,
  UserCoffeeStatsResponseBody,
  UserLoginRequestBody,
  UserLoginResponseBody,
  UserRegisterRequestBody,
  UserUpdateNicknameRequestBody,
  UserUpdatePasswordRequestBody,
  GoogleLoginRequestBody,
  UserEmailSendRequestBody,
  EmailCodeVerifyRequestBody,
  AccessTokenRefreshResponseBody,
} from 'types/userType';
import {
  AttendancesResponseBody,
  PostUserAttendanceRequestBody,
  PostUserGeolocationAttendanceRequestBody,
} from 'types/attendanceType';
import request from '../utils/request';

export const postEmailSendApi = (payload: UserEmailSendRequestBody) =>
  request<{ expiredTime: number }>(`/email/send`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
    credentials: 'include',
  });

export const postVerifyCodeAPi = (payload: EmailCodeVerifyRequestBody) =>
  request(`/email/verify`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
    credentials: 'include',
  });

export const submitRegisterApi = async (payload: UserRegisterRequestBody) => {
  await request<{ accessToken: string }>('/users', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
    credentials: 'include',
  });

  const { nickname, ...loginRequestBody } = payload;

  return submitLoginApi(loginRequestBody);
};

export const submitLoginApi = async (payload: UserLoginRequestBody) =>
  request<UserLoginResponseBody>('/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

export const googleLoginApi = async ({ code }: GoogleLoginRequestBody) =>
  request<UserLoginResponseBody>(`/login/oauth2/google?code=${code}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

export const getAttendancesApi = (id: number | undefined) => () => {
  return request<AttendancesResponseBody>(`/meetings/${id}/attendances/today`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

export const postUserAttendanceApi = async ({
  meetingId,
  userId,
  isPresent,
}: PostUserAttendanceRequestBody) => {
  return request<{}>(
    `/meetings/${meetingId}/users/${userId}/attendances/today`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ isPresent }),
    }
  );
};

export const postUserGeolocationAttendanceApi =
  () =>
  async ({
    meetingId,
    userId,
    latitude,
    longitude,
  }: PostUserGeolocationAttendanceRequestBody) => {
    return request<{}>(
      `/meetings/${meetingId}/users/${userId}/attendances/today/geolocation`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ latitude, longitude }),
      }
    );
  };

export const getUserCoffeeStatsApi = (id: string | undefined) => async () => {
  return request<UserCoffeeStatsResponseBody>(`/meetings/${id}/coffees/use`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

export const updateNicknameApi =
  () => async (payload: UserUpdateNicknameRequestBody) => {
    return request('/users/me/nickname', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });
  };

export const updatePasswordApi =
  () => async (payload: UserUpdatePasswordRequestBody) => {
    return request('/users/me/password', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });
  };

export const unregisterApi =
  () => async (payload: { password: User['password'] }) => {
    return request('/users/me', {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });
  };

export const accessTokenRefreshApi = () =>
  request<AccessTokenRefreshResponseBody>('/token/refresh', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

export const logoutApi = ({}) =>
  request(`/token/logout`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });
