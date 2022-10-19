import { DELAY } from 'mocks/configs';
import {
  checkExpiredToken,
  extractIdFromCookie,
  generateAccessToken,
  generateRefreshToken,
} from 'mocks/utils';
import { rest } from 'msw';

export default [
  rest.get(`${process.env.API_SERVER_HOST}/token/refresh`, (req, res, ctx) => {
    const { id, expiredTimestamp, isExist, isValidToken } =
      extractIdFromCookie(req);

    if (!isExist || !id || !expiredTimestamp) {
      return res(
        ctx.status(401),
        ctx.json({
          message: '토큰이 존재하지 않습니다.',
          tokenStatus: 'empty',
        }),
        ctx.delay(DELAY)
      );
    }

    if (!isValidToken || checkExpiredToken(expiredTimestamp)) {
      return res(
        ctx.status(401),
        ctx.json({
          message: '유효하지 않거나 만료된 토큰입니다',
          tokenStatus: 'invalid',
        }),
        ctx.delay(DELAY)
      );
    }

    const accessToken = generateAccessToken(id);
    const refreshToken = generateRefreshToken(id);

    return res(
      ctx.status(200),
      ctx.json({
        accessToken,
      }),
      ctx.cookie('refreshToken', refreshToken),
      ctx.delay(DELAY)
    );
  }),
];
