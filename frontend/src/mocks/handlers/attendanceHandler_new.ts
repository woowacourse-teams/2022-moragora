import { DefaultBodyType, rest } from 'msw';
import users from 'mocks/fixtures/users';
import meetings from 'mocks/fixtures/meetings_new';
import { DELAY } from 'mocks/configs';
import { extractIdFromHeader } from 'mocks/utils';
import { Attendance } from 'types/attendanceType_new';
import { User } from 'types/userType_new';

type MeetingPathParams = {
  meetingId: string;
  userId: string;
};

export default [
  rest.get<DefaultBodyType, Pick<MeetingPathParams, 'meetingId'>>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/attendances/today`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' }),
          ctx.delay(DELAY)
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' }),
          ctx.delay(DELAY)
        );
      }

      const { meetingId } = req.params;
      const meeting = meetings.find(({ id }) => id === Number(meetingId));

      if (!meeting) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      try {
        const userAttendances: Attendance[] = meeting.userIds.map((userId) => {
          const matchedUser = users.find((user) => user.id === userId);
          const { email, password, accessToken, ...user } = matchedUser as User;

          return {
            ...user,
            attendanceStatus: 'none',
          };
        });

        return res(
          ctx.status(200),
          ctx.json({ users: userAttendances }),
          ctx.delay(DELAY)
        );
      } catch (e) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }
    }
  ),

  rest.post<{ isPresent: boolean }, MeetingPathParams>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/users/:userId/attendances/today`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);
      const { meetingId, userId } = req.params;

      if (!token.isValidToken || token.id !== Number(userId)) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' }),
          ctx.delay(DELAY)
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' }),
          ctx.delay(DELAY)
        );
      }

      const meeting = meetings.find(({ id }) => id === Number(meetingId));

      if (!meeting) {
        return res(
          ctx.status(404),
          ctx.json({ message: '미팅 방이 존재하지 않습니다.' }),
          ctx.delay(DELAY)
        );
      }

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),
];
