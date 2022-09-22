import { AuthProvider, User } from 'types/userType';

const LENGTH = 100;

const generateIndexes = (length: number) => Array.from({ length });

const users = generateIndexes(LENGTH).map<User>((_, id) => ({
  id,
  email: `user${id}@google.com`,
  password: `user${id}pw!`,
  nickname: `user${id}`,
  accessToken: null,
  authProvider:
    id === LENGTH - 1 ? AuthProvider['google'] : AuthProvider['checkmate'],
}));

export default users;
