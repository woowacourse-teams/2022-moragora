import { DefaultBodyType, rest } from 'msw';
import discussions from '../fixtures/Discussions';
import * as T from '../../types/DiscussionTypes';

type DiscussionsPathParams = {
  discussionId: string;
};

const DELAY = 700;

export default [
  rest.get('/discussions', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ discussions }), ctx.delay(DELAY));
  }),

  rest.get<DefaultBodyType, DiscussionsPathParams>(
    '/discussions/:discussionId',
    (req, res, ctx) => {
      const { discussionId } = req.params;
      const discussionIdConvertedToNumber = Number(discussionId);
      const discussion = discussions.find(
        ({ id }) => id === discussionIdConvertedToNumber
      );

      if (!discussion) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      return res(ctx.status(200), ctx.json(discussion), ctx.delay(DELAY));
    }
  ),

  rest.post<T.DiscussionsRequestBody>('/discussions', (req, res, ctx) => {
    if (!req.body.title || !req.body.content) {
      return res(ctx.status(400), ctx.delay(DELAY));
    }

    const newDiscussion: T.Discussion = {
      ...req.body,
      id: discussions.length + 1,
      views: 1,
      createdAt: Date.now(),
      updatedAt: null,
    };

    discussions.push(newDiscussion);

    return res(
      ctx.status(201),
      ctx.set('Location', `discussions/${newDiscussion.id}`),
      ctx.delay(DELAY)
    );
  }),
];
