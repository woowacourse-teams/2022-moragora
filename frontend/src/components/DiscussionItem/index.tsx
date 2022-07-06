import React from 'react';
import * as S from './DiscussionItem.styled';

type Discussion = {
  id: number;
  title: string;
  content: string;
  views: number;
  createdAt: number;
  updatedAt: number | null;
};

type DiscussionProps = {
  discussion: Discussion;
};

const DiscussionItem: React.FC<DiscussionProps> = ({ discussion }) => {
  const elapsedTimeFromCreatedAt = Date.now() - discussion.createdAt;

  return (
    <S.Layout>
      <S.Title>{discussion.title}</S.Title>
      <S.Box>
        <S.TimestampParagraph>
          {Math.floor(elapsedTimeFromCreatedAt / 1000)}초 전
          {!!discussion.updatedAt && <S.UpdatedSpan>수정됨</S.UpdatedSpan>}
        </S.TimestampParagraph>
        <S.OpinionCountBox>
          <S.OpinionsSVG
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={2}
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"
            />
          </S.OpinionsSVG>
          <S.OpinionCountSpan>{discussion.views}</S.OpinionCountSpan>
        </S.OpinionCountBox>
      </S.Box>
    </S.Layout>
  );
};

export default DiscussionItem;
