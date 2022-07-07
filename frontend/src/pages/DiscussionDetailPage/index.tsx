import React, { useState } from 'react';
import { css } from '@emotion/react';
import * as S from './DiscussionDetailPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import DialogButton from '../../components/@shared/DialogButton';
import useFetch from '../../hooks/useFetch';
import * as T from '../../types/DiscussionTypes';

const DiscussionDetailPage = () => {
  const [isEditingOpinion, setIsEditingOpinion] = useState(false);
  const {
    data: discussion,
    loading,
    error,
  } = useFetch<T.Discussion>('/discussions/1');

  if (loading) {
    return <>Loading...</>;
  }

  if (error) {
    return <>Error...</>;
  }

  return (
    <>
      <S.Layout>
        <S.DiscussionDetailSection>
          <S.DiscussionTitle>{discussion.title}</S.DiscussionTitle>
          <S.DiscussionInformationBox>
            <S.Paragraph>
              익명
              <S.TimestampSpan>{7}시간 전</S.TimestampSpan>
            </S.Paragraph>
            <S.ControlButtonBox>
              <S.ControlButton type="button">수정</S.ControlButton>
              <S.ControlButton type="button">삭제</S.ControlButton>
            </S.ControlButtonBox>
          </S.DiscussionInformationBox>
          <S.DiscussionContentBox>
            <p
              css={css`
                margin: 0;
              `}
            >
              {discussion.content}
            </p>
          </S.DiscussionContentBox>
        </S.DiscussionDetailSection>
        <S.OpinionListSection>
          <S.OpinionCountParagraph>0개의 의견</S.OpinionCountParagraph>
        </S.OpinionListSection>
      </S.Layout>
      <Footer>
        {isEditingOpinion ? (
          <>
            <textarea
              css={css`
                width: 100%;
                height: 6rem;
              `}
            />
            <S.OpinionEditorButtonBox>
              <DialogButton
                type="button"
                variant="dismiss"
                onClick={() => {
                  setIsEditingOpinion(false);
                }}
              >
                취소
              </DialogButton>
              <DialogButton type="button" variant="confirm">
                확인
              </DialogButton>
            </S.OpinionEditorButtonBox>
          </>
        ) : (
          <Button
            type="button"
            onClick={() => {
              setIsEditingOpinion(true);
            }}
          >
            의견 작성
          </Button>
        )}
      </Footer>
    </>
  );
};

export default DiscussionDetailPage;
