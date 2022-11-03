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
import { privateRequest, publicRequest } from './instances';

export const postEmailSendApi = (payload: UserEmailSendRequestBody) =>
  publicRequest.post<{ expiredTime: number }>(`/email/send`, payload, {
    withCredentials: true,
  });

export const postVerifyCodeAPi = (payload: EmailCodeVerifyRequestBody) =>
  publicRequest.post(`/email/verify`, payload, { withCredentials: true });

export const submitLoginApi = async (payload: UserLoginRequestBody) => {
  return publicRequest.post<UserLoginResponseBody>('/login', payload, {
    withCredentials: true,
  });
};

export const submitRegisterApi = async (payload: UserRegisterRequestBody) => {
  await publicRequest.post<{ accessToken: string }>('/users', payload, {
    withCredentials: true,
  });

  const { nickname, ...loginRequestBody } = payload;

  return submitLoginApi(loginRequestBody);
};

export const googleLoginApi = async ({ code }: GoogleLoginRequestBody) =>
  publicRequest.post<UserLoginResponseBody>(
    `/login/oauth2/google?code=${code}`,
    null,
    {
      withCredentials: true,
    }
  );

export const getAttendancesApi = (id: number | undefined) => () => {
  return privateRequest<AttendancesResponseBody>(
    `/meetings/${id}/attendances/today`
  );
};

export const postUserAttendanceApi = async ({
  meetingId,
  userId,
  isPresent,
}: PostUserAttendanceRequestBody) => {
  return privateRequest.post<{}>(
    `/meetings/${meetingId}/users/${userId}/attendances/today`,
    { isPresent }
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
    return privateRequest.post<{}>(
      `/meetings/${meetingId}/users/${userId}/attendances/today/geolocation`,
      { latitude, longitude }
    );
  };

export const getLoginUserDataApi = () => async () => {
  return privateRequest.get<GetLoginUserDataResponseBody>('/users/me');
};

export const getUserCoffeeStatsApi = (id: string | undefined) => async () => {
  return privateRequest.get<UserCoffeeStatsResponseBody>(
    `/meetings/${id}/coffees/use`
  );
};

export const updateNicknameApi =
  () => async (payload: UserUpdateNicknameRequestBody) => {
    return privateRequest.put('/users/me/nickname', payload);
  };

export const updatePasswordApi =
  () => async (payload: UserUpdatePasswordRequestBody) => {
    return privateRequest.put('/users/me/password', payload);
  };

export const unregisterApi =
  () => async (payload: { password: User['password'] }) => {
    return privateRequest.delete('/users/me', {
      data: payload,
    });
  };

export const accessTokenRefreshApi = () =>
  publicRequest.get<AccessTokenRefreshResponseBody>('/token/refresh', {
    withCredentials: true,
  });

export const logoutApi = () =>
  publicRequest.post(`/token/logout`, null, {
    withCredentials: true,
  });
