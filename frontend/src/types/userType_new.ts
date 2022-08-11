export type AttendanceStatus = 'none' | 'present' | 'tardy';

export type User = {
  id: number;
  email: string;
  password: string;
  nickname: string;
  accessToken: string | null;
};

export type Participant = Omit<User, 'password' | 'accessToken'> & {
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

export type GetLoginUserDataResponseBody = Pick<
  User,
  'id' | 'nickname' | 'email'
>;

export type UserCoffeeStatsResponseBody = {
  userCoffeeStats: (Pick<User, 'id' | 'nickname'> & {
    coffeeCount: number;
  })[];
};
