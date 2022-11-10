export enum AuthProvider {
  checkmate = 'checkmate',
  google = 'google',
}

export enum TokenStatus {
  expired = 'expired',
  invalid = 'invalid',
  empty = 'empty',
}

export type AttendanceStatus = 'none' | 'present' | 'tardy';

export type User = {
  id: number;
  email: string;
  password: string;
  nickname: string;
  accessToken: string | null;
  authProvider: keyof typeof AuthProvider;
};

export type Participant = Pick<User, 'id' | 'email' | 'nickname'> & {
  tardyCount: number;
  attendanceStatus: AttendanceStatus;
};

export type UserQueryWithKeywordResponse = Pick<
  User,
  'id' | 'email' | 'nickname'
>;

export type UserRegisterRequestBody = Pick<
  User,
  'email' | 'nickname' | 'password'
>;

export type UserLoginRequestBody = Pick<User, 'email' | 'password'>;

export type UserLoginResponseBody = Pick<User, 'accessToken'>;

export type GoogleLoginRequestBody = { code: string };

export type GetLoginUserDataResponseBody = Omit<
  User,
  'password' | 'accessToken'
>;

export type UserUpdateNicknameRequestBody = Pick<User, 'nickname'>;

export type UserUpdatePasswordRequestBody = {
  oldPassword: User['password'];
  newPassword: User['password'];
};

export type UserDeleteRequestBody = Pick<User, 'password'>;

export type UserCoffeeStatsResponseBody = {
  userCoffeeStats: (Pick<User, 'id' | 'nickname'> & {
    coffeeCount: number;
  })[];
};

export type UserEmailSendRequestBody = Pick<User, 'email'>;

export type EmailCodeVerifyRequestBody = {
  email: User['email'];
  verifyCode: string;
};

export type AccessTokenRefreshResponseBody = Pick<User, 'accessToken'>;

export type ErrorResponseBody = {
  message: string;
  tokenStatus?: keyof typeof TokenStatus;
};
