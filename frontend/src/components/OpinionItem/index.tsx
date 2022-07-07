import React from 'react';
import * as S from './OpinionItem.styled';
import * as T from '../../types/OpinionTypes';

type OpinionItemProps = {
  opinion: T.Opinion;
};

const OpinionItem: React.FC<OpinionItemProps> = ({ opinion }) => {
  return (
    <S.Layout>
      <S.Box>
        <S.nameSpan>익명</S.nameSpan>
        <S.TimestampParagraph>
          7시간 전{!!opinion.updatedAt && <S.UpdatedSpan>수정됨</S.UpdatedSpan>}
        </S.TimestampParagraph>
      </S.Box>
      <S.Box>
        <S.OpinionParagraph>{opinion.content}</S.OpinionParagraph>
      </S.Box>
    </S.Layout>
  );
};

export default OpinionItem;
