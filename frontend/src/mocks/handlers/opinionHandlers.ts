import { DefaultBodyType, rest } from 'msw';
import discussions from '../fixtures/Discussions';
import opinions from '../fixtures/Opinions';
import * as T from '../../types/OpinionTypes';

type DiscussionsPathParams = {
  discussionId: string;
};

const DELAY = 700;

export default [
  rest.get<DefaultBodyType, DiscussionsPathParams>(
    '/discussions/:discussionId/opinions',
    (req, res, ctx) => {
      const { discussionId } = req.params;
      const discussionIdConvertedToNumber = Number(discussionId);
      const discussion = discussions.find(
        ({ id }) => id === discussionIdConvertedToNumber
      );

      if (!discussion) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      const filteredOpinions = opinions.filter(
        (opinion) => opinion.discussionId === discussionIdConvertedToNumber
      );

      return res(
        ctx.status(200),
        ctx.json({ opinions: filteredOpinions }),
        ctx.delay(DELAY)
      );
    }
  ),

  rest.post<T.OpinionRequestBody, DiscussionsPathParams>(
    '/discussions/:discussionId/opinions',
    (req, res, ctx) => {
      const { discussionId } = req.params;
      const discussionIdConvertedToNumber = Number(discussionId);
      const discussion = discussions.find(
        ({ id }) => id === discussionIdConvertedToNumber
      );

      if (!discussion) {
        return res(ctx.status(404), ctx.delay(DELAY));
      }

      if (!req.body.content) {
        return res(ctx.status(400), ctx.delay(DELAY));
      }

      const newOpinion: T.Opinion = {
        ...req.body,
        id: discussions.length + 1,
        discussionId: discussionIdConvertedToNumber,
        createdAt: Date.now(),
        updatedAt: null,
      };

      opinions.push(newOpinion);

      return res(
        ctx.status(201),
        ctx.set(
          'Location',
          `/discussions/${discussionId}/opinions/${newOpinion.id}`
        ),
        ctx.delay(DELAY)
      );
    }
  ),
];
