import { rest } from 'msw';
import { DELAY } from 'mocks/configs';

export default [
  rest.get(`${process.env.API_SERVER_HOST}/server-time`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({ serverTime: Date.now() }),
      ctx.delay(DELAY)
    );
  }),
];
