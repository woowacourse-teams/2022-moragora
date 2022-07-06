import React, { useState } from 'react';
import { css } from '@emotion/react';
import * as S from './DiscussionDetailPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import DialogButton from '../../components/@shared/DialogButton';

const DiscussionDetailPage = () => {
  const [isEditingOpinion, setIsEditingOpinion] = useState(false);

  return (
    <>
      <S.Layout>
        <S.DiscussionDetailSection>
          <S.DiscussionTitle>우아한형제들 vs 용감한형제들</S.DiscussionTitle>
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
              안녕하세요 여러분은 어떻게 생각하시나요? 안녕하세요 여러분은
              어떻게 생각하시나요? 안녕하세요 여러분은 어떻게 생각하시나요?
              안녕하세요 여러분은 어떻게 생각하시나요?
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
