import { rest } from 'msw';
import users from 'mocks/fixtures/users';
import {
  UserRegisterRequestBody,
  UserLoginRequestBody,
  User,
  AuthProvider,
  UserEmailSendRequestBody,
  EmailCodeVerifyRequestBody,
} from 'types/userType';
import { DELAY } from 'mocks/configs';
import {
  checkExpiredToken,
  extractIdFromHeader,
  generateAccessToken,
  generateRefreshToken,
} from 'mocks/utils';
import {
  UserDeleteRequestBody,
  UserUpdateNicknameRequestBody,
  UserUpdatePasswordRequestBody,
} from 'types/userType';
import meetings from 'mocks/fixtures/meetings';

const emailReg =
  /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.([a-zA-Z])+$/;
const nicknameReg = /^([a-zA-Z0-9가-힣]){1,15}$/;
const passwordReg =
  /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,30}$/;

let emailExpiredTime: number = 0;

export default [
  rest.post<UserRegisterRequestBody>(
    `${process.env.API_SERVER_HOST}/users`,
    (req, res, ctx) => {
      const { email, nickname, password } = req.body;

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
        authProvider: AuthProvider['checkmate'],
      };

      users.push(newUser);

      return res(ctx.status(201), ctx.delay(DELAY));
    }
  ),

  rest.post<UserLoginRequestBody>(
    `${process.env.API_SERVER_HOST}/login`,
    (req, res, ctx) => {
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

      const accessToken = generateAccessToken(targetUser.id);
      const refreshToken = generateRefreshToken(targetUser.id);

      targetUser.accessToken = accessToken;

      return res(
        ctx.status(200),
        ctx.json({
          accessToken: targetUser.accessToken,
        }),
        ctx.cookie('refreshToken', refreshToken),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.post(
    `${process.env.API_SERVER_HOST}/login/oauth2/google`,
    (req, res, ctx) => {
      const code = req.url.searchParams.get('code');

      if (!code) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 인증코드입니다.' })
        );
      }

      const targetUser = users[users.length - 1];
      const accessToken = generateAccessToken(targetUser.id);
      const refreshToken = generateRefreshToken(targetUser.id);

      targetUser.accessToken = accessToken;

      return res(
        ctx.status(200),
        ctx.cookie('refreshToken', refreshToken),
        ctx.json({
          accessToken,
        }),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.get(`${process.env.API_SERVER_HOST}/users`, (req, res, ctx) => {
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

  rest.get(`${process.env.API_SERVER_HOST}/users/me`, (req, res, ctx) => {
    const token = extractIdFromHeader(req);

    if (!token.isValidToken) {
      return res(
        ctx.status(401),
        ctx.json({
          message: '유효하지 않은 토큰입니다.',
          tokenStatus: 'invalid',
        })
      );
    }

    if (checkExpiredToken(token.expiredTimestamp)) {
      return res(
        ctx.status(401),
        ctx.json({ message: '만료된 토큰입니다.', tokenStatus: 'expired' })
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
        authProvider: user.authProvider,
      }),
      ctx.delay(DELAY)
    );
  }),

  rest.put<UserUpdateNicknameRequestBody>(
    `${process.env.API_SERVER_HOST}/users/me/nickname`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);
      const { nickname } = req.body;

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({
            message: '유효하지 않은 토큰입니다.',
            tokenStatus: 'invalid',
          })
        );
      }

      if (checkExpiredToken(token.expiredTimestamp)) {
        return res(
          ctx.status(401),
          ctx.json({ message: '만료된 토큰입니다.', tokenStatus: 'expired' })
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' })
        );
      }

      user.nickname = nickname;

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),

  rest.put<UserUpdatePasswordRequestBody>(
    `${process.env.API_SERVER_HOST}/users/me/password`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);
      const { oldPassword, newPassword } = req.body;

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({
            message: '유효하지 않은 토큰입니다.',
            tokenStatus: 'invalid',
          })
        );
      }

      if (checkExpiredToken(token.expiredTimestamp)) {
        return res(
          ctx.status(401),
          ctx.json({ message: '만료된 토큰입니다.', tokenStatus: 'expired' })
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' })
        );
      }

      if (user.password !== oldPassword) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '비밀번호가 올바르지 않습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      if (oldPassword === newPassword) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '새로운 비밀번호가 기존의 비밀번호와 일치합니다',
          }),
          ctx.delay(DELAY)
        );
      }

      user.password = newPassword;

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),

  rest.delete<UserDeleteRequestBody>(
    `${process.env.API_SERVER_HOST}/users/me`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);
      const { password } = req.body;

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({
            message: '유효하지 않은 토큰입니다.',
            tokenStatus: 'invalid',
          })
        );
      }

      if (checkExpiredToken(token.expiredTimestamp)) {
        return res(
          ctx.status(401),
          ctx.json({ message: '만료된 토큰입니다.', tokenStatus: 'expired' })
        );
      }

      const userIndex = users.findIndex(({ id }) => id === token.id);

      if (userIndex === -1) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' })
        );
      }

      if (users[userIndex].password !== password) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '비밀번호가 올바르지 않습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      if (meetings.some(({ masterId }) => masterId === users[userIndex].id)) {
        return res(
          ctx.status(403),
          ctx.json({
            message: '마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      users.splice(userIndex, 1);

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),

  rest.post<UserEmailSendRequestBody>(
    `${process.env.API_SERVER_HOST}/email/send`,
    (req, res, ctx) => {
      const { email } = req.body;

      if (!emailReg.test(email)) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '이메일 형식이 올바르지 않습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      const isExist = users.some(
        ({ email: existedEmail }) => email === existedEmail
      );

      if (isExist) {
        return res(
          ctx.status(409),
          ctx.json({
            message: '중복된 이메일입니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      const timer = new Date();
      timer.setMinutes(timer.getMinutes() + 5);
      emailExpiredTime = timer.getTime();

      return res(
        ctx.status(200),
        ctx.json({
          expiredTime: emailExpiredTime,
        }),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.post<EmailCodeVerifyRequestBody>(
    `${process.env.API_SERVER_HOST}/email/verify`,
    (req, res, ctx) => {
      const { email, verifyCode } = req.body;
      const authCode = '123456';
      const now = new Date().getTime();

      if (!emailReg.test(email)) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '이메일 형식이 올바르지 않습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      if (verifyCode !== authCode) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '인증코드가 올바르지 않습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      if (emailExpiredTime < now) {
        return res(
          ctx.status(400),
          ctx.json({
            message: '인증코드가 만료되었습니다.',
          }),
          ctx.delay(DELAY)
        );
      }

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),

  rest.post(`${process.env.API_SERVER_HOST}/token/logout`, (req, res, ctx) => {
    const timer = new Date();
    timer.setSeconds(timer.getSeconds() + 1);

    return res(
      ctx.status(204),
      ctx.cookie('refreshToken', '', { expires: timer }),
      ctx.delay(DELAY)
    );
  }),
];
