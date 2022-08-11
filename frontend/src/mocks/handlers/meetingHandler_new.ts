import { DefaultBodyType, rest } from 'msw';
import meetings from 'mocks/fixtures/meetings_new';
import users from 'mocks/fixtures/users';
import {
  AttendanceStatus,
  UserCoffeeStatsResponseBody,
} from 'types/userType_new';
import {
  MeetingListResponseBody,
  MeetingCreateRequestBody,
  MeetingResponseBody,
} from 'types/meetingType_new';
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
      meetings: myMeetings.map(
        ({ attendanceEventCount, userIds, ...meeting }) => ({
          ...meeting,
          isLoginUserMaster: false,
          isCoffeeTime: true,
          tardyCount: 3,
          upcomingEvent: {
            id: 1,
            attendanceOpenTime: '09:30',
            attendanceClosedTime: '10:05',
            meetingStartTime: '10:00',
            meetingEndTime: '18:00',
            date: '2022-08-08',
          },
        })
      ),
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
        isLoginUserMaster: true,
        isCoffeeTime,
        isActive: true,
        users: joinedUsers.map((user) => ({ ...user, isMaster: false })),
      };

      const responseBody: MeetingResponseBody = joinedMeeting;

      return res(ctx.status(200), ctx.json(responseBody), ctx.delay(DELAY));
    }
  ),

  rest.post<MeetingCreateRequestBody>(
    `${process.env.API_SERVER_HOST}/meetings`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);

      if (!token.isValidToken || !token.id) {
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
      const meetingId = meetings.length;

      meetings.push({
        ...meeting,
        id: meetingId,
        isActive: false,
        attendanceEventCount: 0,
        masterId: token.id,
      });

      return res(
        ctx.status(201),
        ctx.set('Location', `/meetings/${meetingId}`),
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
