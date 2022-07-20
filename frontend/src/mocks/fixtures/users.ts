export type User = {
  id: number;
  email: string;
  password: string;
  nickname: string;
  accessToken: string | null;
  attendanceStatus: 'present' | 'tardy';
};

const generateIndexes = (length: number) => Array.from({ length });

const users = generateIndexes(100).map<User>((_, id) => ({
  id,
  email: `user${id}@google.com`,
  password: `user${id}pw!`,
  nickname: `user${id}`,
  accessToken: null,
  attendanceStatus: 'tardy',
}));

export default users;
