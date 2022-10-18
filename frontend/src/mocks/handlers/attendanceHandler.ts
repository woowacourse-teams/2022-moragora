import { DefaultBodyType, rest } from 'msw';
import users from 'mocks/fixtures/users';
import meetings from 'mocks/fixtures/meetings';
import beacons from 'mocks/fixtures/beacons';
import { Beacon } from 'types/beaconType';
import { DELAY } from 'mocks/configs';
import { checkExpiredToken, extractIdFromHeader } from 'mocks/utils';
import { Attendance } from 'types/attendanceType';
import { User } from 'types/userType';

type MeetingPathParams = {
  meetingId: string;
  userId: string;
};

let tempBeacons = beacons;

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

      if (checkExpiredToken(token.expiredTimestamp)) {
        return res(
          ctx.status(401),
          ctx.json({ message: '만료된 토큰입니다.', tokenStatus: 'expired' })
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

      if (checkExpiredToken(token.expiredTimestamp)) {
        return res(
          ctx.status(401),
          ctx.json({ message: '만료된 토큰입니다.', tokenStatus: 'expired' })
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

  rest.post<{ latitude: number; longitude: number }, MeetingPathParams>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/users/:userId/attendances/today/geolocation`,
    (req, res, ctx) => {
      const token = extractIdFromHeader(req);
      const { meetingId, userId } = req.params;
      const { latitude, longitude } = req.body;

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

      function getDistanceFromLatLonInMeter(
        lat1: number,
        lng1: number,
        lat2: number,
        lng2: number
      ) {
        function deg2rad(deg: number) {
          return deg * (Math.PI / 180);
        }

        const R = 6371; // Radius of the earth in km
        const dLat = deg2rad(lat2 - lat1); // deg2rad below
        const dLon = deg2rad(lng2 - lng1);
        const a =
          Math.sin(dLat / 2) * Math.sin(dLat / 2) +
          Math.cos(deg2rad(lat1)) *
            Math.cos(deg2rad(lat2)) *
            Math.sin(dLon / 2) *
            Math.sin(dLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        const d = R * c * 1000; // Distance in meter

        return d;
      }

      const beacons: Beacon[] = tempBeacons.filter(
        (beacon) => meeting.id === beacon.meetingId
      );
      const distances = beacons.map((beacon) => {
        return {
          ...beacon,
          distance: getDistanceFromLatLonInMeter(
            beacon.latitude,
            beacon.longitude,
            latitude,
            longitude
          ),
        };
      });

      if (
        distances.length >= 1 &&
        distances.some(({ radius, distance }) => distance <= radius)
      ) {
        return res(ctx.status(204), ctx.delay(DELAY));
      }

      return res(ctx.status(400), ctx.json({ beacons }), ctx.delay(DELAY));
    }
  ),
];
