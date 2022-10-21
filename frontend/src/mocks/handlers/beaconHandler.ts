import { rest } from 'msw';
import meetings from 'mocks/fixtures/meetings';
import users from 'mocks/fixtures/users';
import beacons from 'mocks/fixtures/beacons';
import { DELAY } from 'mocks/configs';
import { extractIdFromHeader } from 'mocks/utils';

let tempBeacons = [...beacons];

type MeetingPathParams = {
  meetingId: string;
};

export default [
  rest.post<
    {
      beacons: {
        latitude: number;
        longitude: number;
        radius: number;
        address: string;
      }[];
    },
    MeetingPathParams
  >(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/beacons`,
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

      const { beacons: newBeacons } = req.body;
      const { meetingId } = req.params;

      const meeting = meetings.find(({ id }) => id === Number(meetingId));

      if (!meeting) {
        return res(
          ctx.status(404),
          ctx.json({ message: '미팅 방이 존재하지 않습니다.' }),
          ctx.delay(DELAY)
        );
      }

      const beaconId = beacons.length;

      tempBeacons.push(
        ...newBeacons.map(
          ({ latitude, longitude, radius, address }, index) => ({
            id: beaconId + index,
            meetingId: Number(meetingId),
            latitude,
            longitude,
            radius,
            address,
          })
        )
      );

      return res(ctx.status(201), ctx.delay(DELAY));
    }
  ),
];
