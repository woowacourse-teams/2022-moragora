import React, { useState } from 'react';
import * as S from './DiscussionDetailPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import DialogButton from '../../components/@shared/DialogButton';
import OpinionItem from '../../components/OpinionItem';
import TextArea from '../../components/@shared/TextArea';
import useFetch from '../../hooks/useFetch';
import * as TDiscussion from '../../types/DiscussionTypes';
import * as TOpinion from '../../types/OpinionTypes';

const postData = (url: string, payload: TOpinion.OpinionRequestBody) => {
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const DiscussionDetailPage = () => {
  const [isEditingOpinion, setIsEditingOpinion] = useState(false);
  const discussionState = useFetch<TDiscussion.Discussion>('/discussions/1');
  const opinionsState = useFetch<{ opinions: TOpinion.Opinion[] }>(
    '/discussions/1/opinions'
  );

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const payload = Object.fromEntries(
      formData.entries()
    ) as TOpinion.OpinionRequestBody;

    postData('/discussions/1/opinions', payload);
  };

  if (discussionState.loading || opinionsState.loading) {
    return <>Loading...</>;
  }

  if (discussionState.error || opinionsState.error) {
    return <>Error...</>;
  }

  return (
    <>
      <S.Layout>
        <S.DiscussionDetailSection>
          <S.DiscussionTitle>{discussionState.data.title}</S.DiscussionTitle>
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
            <S.Paragraph>{discussionState.data.content}</S.Paragraph>
          </S.DiscussionContentBox>
        </S.DiscussionDetailSection>
        <S.OpinionListSection>
          <S.OpinionCountParagraph>
            {opinionsState.data.opinions.length}개의 의견
          </S.OpinionCountParagraph>
          <S.DivideLine />
          <S.Table>
            <S.TableRow>
              {opinionsState.data.opinions.map((opinion) => (
                <S.TabelData key={opinion.id}>
                  <OpinionItem opinion={opinion} />
                  <S.DivideLine />
                </S.TabelData>
              ))}
            </S.TableRow>
          </S.Table>
        </S.OpinionListSection>
      </S.Layout>
      <Footer>
        {isEditingOpinion ? (
          <form onSubmit={handleSubmit}>
            <TextArea name="content" />
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
              <DialogButton type="submit" variant="confirm">
                확인
              </DialogButton>
            </S.OpinionEditorButtonBox>
          </form>
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
