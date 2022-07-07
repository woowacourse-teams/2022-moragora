import { rest } from 'msw';
import meeting from '../fixtures/meeting';

type MeetingAttendanceRequestBody = {
  id: number;
  isAbsent: boolean;
}[];

const DELAY = 700;

export default [
  rest.get('/meetings/1', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(meeting), ctx.delay(DELAY));
  }),

  rest.patch<MeetingAttendanceRequestBody>('/meetings/1', (req, res, ctx) => {
    if (
      req.body.some(({ id, isAbsent }) => !id || typeof isAbsent === 'boolean')
    ) {
      return res(ctx.status(400), ctx.delay(DELAY));
    }

    return res(ctx.status(204), ctx.delay(DELAY));
  }),
];
