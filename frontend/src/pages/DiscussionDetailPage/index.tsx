import React from 'react';
import * as S from './DiscussionDetailPage.styled';

const DiscussionDetailPage = () => {
  return (
    <S.Layout>
      <S.DicussionDetailSection>
        <S.DiscussionTitle>우아한형제들 vs 용감한형제들</S.DiscussionTitle>
      </S.DicussionDetailSection>
      <S.OpinionListSection>Opinions</S.OpinionListSection>
    </S.Layout>
  );
};

export default DiscussionDetailPage;
