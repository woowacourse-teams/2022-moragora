import { DefaultBodyType, rest } from 'msw';
import meetings from '../fixtures/meeting';
import users from '../fixtures/users';
import { UserAttendanceCheckRequestBody } from 'types/userType';

const DELAY = 700;

type MeetingPathParams = {
  meetingId: string;
  userId: string;
};

export default [
  rest.get<DefaultBodyType, Pick<MeetingPathParams, 'meetingId'>>(
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
            attendanceStatus: true,
            tardyCount: 3,
          };
        }),
      };

      return res(ctx.status(200), ctx.json(joinedMeeting), ctx.delay(DELAY));
    }
  ),

  rest.put<UserAttendanceCheckRequestBody, MeetingPathParams>(
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

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),
];
