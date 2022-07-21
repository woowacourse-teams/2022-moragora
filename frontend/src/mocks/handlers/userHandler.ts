import { rest } from 'msw';
import users from 'mocks/fixtures/users';
import {
  UserRegisterRequestBody,
  UserLoginRequestBody,
  User,
} from 'types/userType';
import { DELAY } from 'mocks/configs';
import { extractIdFromHeader, generateToken } from 'mocks/utils';

export default [
  rest.post<UserRegisterRequestBody>('/users', (req, res, ctx) => {
    const { email, nickname, password } = req.body;

    const emailReg =
      /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.([a-zA-Z])+$/;
    const nicknameReg = /^([a-zA-Z0-9가-힣]){1,15}$/;
    const passwordReg =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,20}$/;

    if (
      !emailReg.test(email) ||
      !nicknameReg.test(nickname) ||
      !passwordReg.test(password)
    ) {
      return res(
        ctx.status(400),
        ctx.json({
          message: '입력 형식이 올바르지 않습니다.',
        }),
        ctx.delay(DELAY)
      );
    }

    if (users.some((user) => user.email === email)) {
      return res(
        ctx.status(400),
        ctx.json({
          message: '중복된 이메일입니다.',
        }),
        ctx.delay(DELAY)
      );
    }

    const newUser: User = {
      id: users.length,
      email,
      password,
      nickname,
      accessToken: null,
    };

    users.push(newUser);

    return res(ctx.status(201), ctx.delay(DELAY));
  }),

  rest.post<UserLoginRequestBody>('/login', (req, res, ctx) => {
    const { email, password } = req.body;

    const targetUser = users.find(
      (user) => user.email === email && user.password === password
    );

    if (!targetUser) {
      return res(
        ctx.status(400),
        ctx.json({
          message: '이메일 혹은 비밀번호가 틀렸습니다.',
        }),
        ctx.delay(DELAY)
      );
    }

    const accessToken = generateToken(targetUser.id);

    targetUser.accessToken = accessToken;

    return res(
      ctx.status(200),
      ctx.json({
        accessToken: targetUser.accessToken,
      }),
      ctx.delay(DELAY)
    );
  }),

  rest.get('/users/check-email', (req, res, ctx) => {
    const email = req.url.searchParams.get('email');
    const isExist = users.some((user) => user.email === email);

    return res(
      ctx.status(200),
      ctx.json({
        isExist,
      }),
      ctx.delay(DELAY)
    );
  }),

  rest.get('/users', (req, res, ctx) => {
    const keyword = req.url.searchParams.get('keyword');
    const queryResult = [...users]
      .splice(30, 10)
      .map(({ id, email, nickname }) => ({
        id,
        email,
        nickname,
      }));

    return res(
      ctx.status(200),
      ctx.json({ users: queryResult }),
      ctx.delay(DELAY)
    );
  }),

  rest.get('/users/me', (req, res, ctx) => {
    const token = extractIdFromHeader(req);

    if (!token.isValidToken) {
      return res(
        ctx.status(401),
        ctx.json({ message: '유효하지 않은 토큰입니다.' })
      );
    }

    const user = users.find(({ id }) => id === token.id);

    if (!user) {
      return res(
        ctx.status(404),
        ctx.json({ message: '유저가 존재하지 않습니다.' })
      );
    }

    return res(
      ctx.status(200),
      ctx.json({
        id: user.id,
        email: user.email,
        nickname: user.nickname,
      }),
      ctx.delay(DELAY)
    );
  }),
];
