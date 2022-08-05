import { User } from 'types/userType';

const generateIndexes = (length: number) => Array.from({ length });

const users = generateIndexes(100).map<User>((_, id) => ({
  id,
  email: `user${id}@google.com`,
  password: `user${id}pw!`,
  nickname: `user${id}`,
  accessToken: null,
}));

export default users;
