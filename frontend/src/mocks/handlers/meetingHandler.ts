import { DefaultBodyType, rest } from 'msw';
import meetings from '../fixtures/meeting';
import users from '../fixtures/users';

type MeetingAttendanceRequestBody = {
  id: number;
  isAbsent: boolean;
}[];

const DELAY = 700;

export default [
  rest.get<DefaultBodyType, { meetingId: string }>(
    '/meetings/:meetingId',
    (req, res, ctx) => {
      const { meetingId } = req.params;

      const meeting = meetings.find(({ id }) => id === Number(meetingId));

      if (!meeting) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      const joinedMeeting = {
        ...meeting,
        users: meeting.userIds.map((id) => users[id]),
      };

      return res(ctx.status(200), ctx.json(joinedMeeting), ctx.delay(DELAY));
    }
  ),

  rest.patch<MeetingAttendanceRequestBody>('/meetings/1', (req, res, ctx) => {
    if (
      req.body.some(({ id, isAbsent }) => !id || typeof isAbsent !== 'boolean')
    ) {
      return res(ctx.status(400), ctx.delay(DELAY));
    }

    return res(ctx.status(204), ctx.delay(DELAY));
  }),
];
