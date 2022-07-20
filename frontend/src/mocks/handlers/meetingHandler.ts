import { DefaultBodyType, rest } from 'msw';
import meetings from 'mocks/fixtures/meeting';
import users from 'mocks/fixtures/users';
import { UserAttendanceCheckRequestBody } from 'types/userType';
import {
  MeetingListResponseBody,
  MeetingCreateRequestBody,
} from 'types/meetingType';
import { addMinute } from 'utils/timeUtil';

const DELAY = 700;

type MeetingPathParams = {
  meetingId: string;
  userId: string;
};

export default [
  rest.get('/meetings/me', (req, res, ctx) => {
    const userId = 6;
    const myMeetings = meetings.filter((meeting) =>
      meeting.userIds.includes(userId)
    );
    const responseBody: MeetingListResponseBody = {
      serverTime: '11:57',
      meetings: myMeetings.map(
        ({ leaveTime, attendanceCount, userIds, ...meeting }) => ({
          ...meeting,
          tardyCount: 3,
        })
      ),
    };

    return res(ctx.status(200), ctx.json(responseBody), ctx.delay(DELAY));
  }),

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
          const { password, accessToken, ...user } = users[id];

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

  rest.post<MeetingCreateRequestBody>('/meetings', (req, res, ctx) => {
    const meeting = req.body;
    const id = meetings.length;

    meetings.push({
      ...meeting,
      id,
      closingTime: addMinute(meeting.entranceTime, 5),
      active: true,
      attendanceCount: 0,
    });

    return res(
      ctx.status(201),
      ctx.set('Location', `/meetings/${id}`),
      ctx.delay(DELAY)
    );
  }),
];
