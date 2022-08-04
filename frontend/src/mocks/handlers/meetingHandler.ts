import { DefaultBodyType, rest } from 'msw';
import meetings from 'mocks/fixtures/meeting';
import users from 'mocks/fixtures/users';
import {
  AttendanceStatus,
  UserAttendanceCheckRequestBody,
  UserCoffeeStatsResponseBody,
} from 'types/userType';
import {
  MeetingListResponseBody,
  MeetingCreateRequestBody,
  MeetingResponseBody,
} from 'types/meetingType';
import { DELAY } from 'mocks/configs';
import { extractIdFromHeader } from 'mocks/utils';

type MeetingPathParams = {
  meetingId: string;
  userId: string;
};

export default [
  rest.get(`${process.env.API_SERVER_HOST}/meetings/me`, (req, res, ctx) => {
    const token = extractIdFromHeader(req);

    if (!token.isValidToken) {
      return res(
        ctx.status(401),
        ctx.json({ message: '유효하지 않은 토큰입니다.' })
      );
    }

    const user = users.find(({ id }) => id === token.id);

    if (!user) {
      return res(
        ctx.status(404),
        ctx.json({ message: '유저가 존재하지 않습니다.' })
      );
    }

    const myMeetings = meetings.filter(({ userIds }) =>
      userIds.includes(user.id)
    );

    const responseBody: MeetingListResponseBody = {
      meetings: myMeetings.map(({ attendanceCount, userIds, ...meeting }) => ({
        ...meeting,
        isMaster: false,
        isCoffeeTime: true,
        tardyCount: 3,
        hasUpcomingEvent: false,
        entranceTime: '10:00',
        closingTime: '10:05',
      })),
    };

    return res(ctx.status(200), ctx.json(responseBody), ctx.delay(DELAY));
  }),

  rest.get<DefaultBodyType, Pick<MeetingPathParams, 'meetingId'>>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' })
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' })
        );
      }

      const { meetingId } = req.params;
      const meeting = meetings.find(({ id }) => id === Number(meetingId));

      if (!meeting) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      const joinedUsers = meeting.userIds.map((id) => {
        const { password, accessToken, ...user } = users[id];

        return {
          ...user,
          attendanceStatus: 'tardy' as AttendanceStatus,
          tardyCount: 3,
        };
      });

      const isCoffeeTime =
        joinedUsers.reduce((next, { tardyCount }) => next + tardyCount, 0) >=
        joinedUsers.length;

      const { userIds, ...joinedMeeting } = {
        ...meeting,
        isMaster: true,
        isCoffeeTime,
        hasUpcomingEvent: true,
        isActive: true,
        entranceTime: '10:00',
        leaveTime: '18:00',
        users: joinedUsers,
      };

      const responseBody: MeetingResponseBody = joinedMeeting;

      return res(ctx.status(200), ctx.json(responseBody), ctx.delay(DELAY));
    }
  ),

  rest.put<UserAttendanceCheckRequestBody, MeetingPathParams>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/users/:userId`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' })
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' })
        );
      }

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

  rest.post<MeetingCreateRequestBody>(
    `${process.env.API_SERVER_HOST}/meetings`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' })
        );
      }

      const user = users.find(({ id }) => id === token.id);

      if (!user) {
        return res(
          ctx.status(404),
          ctx.json({ message: '유저가 존재하지 않습니다.' })
        );
      }

      const meeting = req.body;
      const id = meetings.length;

      meetings.push({
        ...meeting,
        id,
        isActive: false,
        attendanceCount: 0,
      });

      return res(
        ctx.status(201),
        ctx.set('Location', `/meetings/${id}`),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.get<
    DefaultBodyType,
    Pick<MeetingPathParams, 'meetingId'>,
    UserCoffeeStatsResponseBody | { message: string }
  >(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/coffees/use`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' })
        );
      }

      const targetMeeting = meetings.find(
        (meeting) => meeting.id === Number(token.id)
      );

      if (!targetMeeting) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      const userCoffeeStats = targetMeeting.userIds.map((id) => {
        const targetUser = users.find((user) => user.id === id);

        return {
          id,
          nickname: targetUser?.nickname || '',
          coffeeCount: 2,
        };
      });

      return res(
        ctx.status(200),
        ctx.json({ userCoffeeStats }),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.post<DefaultBodyType, Pick<MeetingPathParams, 'meetingId'>>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/coffees/use`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken) {
        return res(
          ctx.status(401),
          ctx.json({ message: '유효하지 않은 토큰입니다.' })
        );
      }

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),
];
