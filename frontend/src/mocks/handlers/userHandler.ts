import { rest } from 'msw';
import users from '../fixtures/users';

type UserRegisterRequestBody = {
  email: string;
  nickname: string;
  password: string;
};

type UserLoginRequestBody = {
  email: string;
  password: string;
};

const DELAY = 700;
const TOKEN_PREFIX = 'badwoody';

const generateToken = (id: number) => `${TOKEN_PREFIX}${id}`;

export default [
  rest.get('/meetings/1/users', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(users), ctx.delay(DELAY));
  }),

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

    const newUser = {
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
];
