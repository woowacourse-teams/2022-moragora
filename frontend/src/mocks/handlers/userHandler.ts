import { rest } from 'msw';
import users from '../fixtures/users';

const DELAY = 700;

export default [
  rest.get('/meetings/1/users', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(users), ctx.delay(DELAY));
  }),
];
