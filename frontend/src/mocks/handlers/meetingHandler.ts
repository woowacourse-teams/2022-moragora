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

      const { userIds, ...joinedMeeting } = {
        ...meeting,
        users: meeting.userIds.map((id) => {
          const { accessToken, ...user } = users[id];

          return {
            ...user,
            tardyCount: 3,
          };
        }),
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

  rest.put<{ attendanceStatus: string }, { meetingId: string; userId: string }>(
    `/meetings/:meetingId/users/:userId`,
    (req, res, ctx) => {
      const { meetingId, userId } = req.params;

      const targetMeeting = meetings.find(
        (meeting) => meeting.id === Number(meetingId)
      );
      if (!targetMeeting) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      const targetUser = users.find((user) => user.id === Number(userId));
      if (!targetUser) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      targetUser.attendanceStatus = req.body.attendanceStatus;

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),
];
