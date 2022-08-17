import { DefaultBodyType, rest } from 'msw';
import users from 'mocks/fixtures/users';
import events from 'mocks/fixtures/events';
import { DELAY } from 'mocks/configs';
import { extractIdFromHeader } from 'mocks/utils';
import {
  EventListResponseBody,
  EventResposeBody,
  MeetingEvent,
} from 'types/eventType';

type PostEventsRequestBody = {
  events: Omit<MeetingEvent, 'id' | 'meetingId'>[];
};

type DeleteEventsRequestBody = {
  eventIds: number[];
};

type MeetingPathParams = {
  meetingId: string;
  userId: string;
};

let tempEvents = [...events];

export default [
  rest.post<PostEventsRequestBody, Pick<MeetingPathParams, 'meetingId'>>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/events`,
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

      const { events: newEvents } = req.body;
      const { meetingId } = req.params;

      tempEvents = tempEvents.map((event) => {
        const matchedEventIndex = newEvents.findIndex(
          (newEvent) => event.date === newEvent.date
        );

        return matchedEventIndex
          ? {
              id: event.id,
              meetingId: Number(meetingId),
              ...newEvents.splice(matchedEventIndex, 1)[0],
            }
          : event;
      });

      const eventsLength = tempEvents.length;

      tempEvents.push(
        ...newEvents.map((event, index) => ({
          ...event,
          meetingId: Number(meetingId),
          id: index + eventsLength,
        }))
      );

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),

  rest.delete<DeleteEventsRequestBody>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/events`,
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

      const { eventIds } = req.body;

      tempEvents = tempEvents.filter((event) => !eventIds.includes(event.id));

      return res(ctx.status(204), ctx.delay(DELAY));
    }
  ),

  rest.get<DefaultBodyType, Pick<MeetingPathParams, 'meetingId'>>(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/events`,
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
      const matchedEvents: EventListResponseBody = tempEvents.filter(
        (event) => event.meetingId === Number(meetingId)
      );

      return res(
        ctx.status(200),
        ctx.json({ events: matchedEvents }),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.get(
    `${process.env.API_SERVER_HOST}/meetings/:meetingId/events/upcoming`,
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
      const upcomingEvent: EventResposeBody | undefined = tempEvents.find(
        (event) => event.meetingId === Number(meetingId)
      );

      if (!upcomingEvent) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      return res(ctx.status(200), ctx.json(upcomingEvent), ctx.delay(DELAY));
    }
  ),
];
