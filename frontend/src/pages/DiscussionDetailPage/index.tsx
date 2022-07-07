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

const opinions: TOpinion.Opinion[] = [
  {
    id: 1,
    content:
      '국민의 자유와 권리는 헌법에 열거되지 아니한 이유로 경시되지 아니한다. 대통령은 제1항과 제2항의 처분 또는 명령을 한 때에는 지체없이 국회에 보고하여 그 승인을 얻어야 한다. 누구든지 체포 또는 구속의 이유와 변호인의 조력을 받을 권리가 있음을 고지받지 아니하고는 체포 또는 구속을 당하지 아니한다. 체포 또는 구속을 당한 자의 가족등 법률이 정하는 자에게는 그 이유와 일시·장소가 지체없이 통지되어야 한다.',
    createdAt: 1657074309874,
    updatedAt: 1657075309874,
  },
  {
    id: 2,
    content:
      '인생을 피가 꽃이 너의 영락과 봄바람이다. 충분히 아름답고 사람은 꽃이 눈에 온갖 너의 별과 것은 봄바람이다. 주는 바로 능히 얼마나 속잎나고, 인간이 되는 가는 끓는다. 어디 밝은 얼음이 가슴이 것이다. 꾸며 어디 뛰노는 부패를 봄바람이다. 그들의 오아이스도 위하여서 가슴이 하는 구하기 가슴에 같은 풀이 피다. 위하여, 청춘이 보내는 청춘의 목숨이 소담스러운 웅대한 쓸쓸하랴? 긴지라 구할 같지 곧 커다란 운다. 구하기 그들은 대중을 황금시대다. 인류의 하여도 인간이 찬미를 목숨이 눈이 봄바람이다. 하여도 피고, 바로 싶이 풀이 때문이다. 우리 풀이 그와 곳이 인도하겠다는 청춘이 황금시대다. 군영과 것이다.보라, 따뜻한 같은 그들은 길지 사막이다. 굳세게 청춘의 피어나기 산야에 불어 피에 고동을 대중을 하는 이것이다. 목숨을 간에 석가는 피고, 위하여 아니다. 인류의 꽃이 남는 할지니, 있는가? 뜨거운지라, 피고, 속에서 청춘을 말이다. 가슴이 많이 꾸며 원대하고, 하였으며, 눈에 구하지 만천하의 반짝이는 위하여서. 들어 따뜻한 내는 그리하였는가? 때에, 대한 얼음 타오르고 싶이 있는 힘있다. 전인 할지니, 어디 꽃 너의 구하기 그들의 쓸쓸하랴? 때에, 원질이 두기 군영과 그들에게 운다. 어디 인간에 생생하며, 그것을 두기 꾸며 있는가? 시들어 풍부하게 자신과 현저하게 그들의 아름다우냐? 위하여서 너의 희망의 오아이스도 것이다. 몸이 보이는 장식하는 튼튼하며, 동산에는 생의 오직 이상이 가슴에 이것이다. 석가는 그들의 두기 이상은 가치를 곧 우리 끝에 따뜻한 그리하였는가? 그러므로 평화스러운 착목한는 창공에 굳세게 약동하다.',
    createdAt: 1657074319874,
    updatedAt: null,
  },
  {
    id: 3,
    content:
      '별빛이 밤이 다 오면 파란 버리었습니다. 말 하나에 별이 나의 가을 계십니다. 그리고 노루, 내린 없이 동경과 라이너 쉬이 까닭입니다. 잔디가 별 말 계집애들의 당신은 멀듯이, 있습니다. 책상을 나의 새겨지는 나의 거외다. 무엇인지 애기 별 위에 불러 있습니다. 시와 소학교 파란 하나에 이제 까닭입니다. 다하지 나의 헤는 했던 사랑과 하나에 가득 마리아 내 봅니다. 나는 가득 마리아 이름과 하나의 있습니다. 둘 별 부끄러운 마리아 듯합니다. 프랑시스 아무 다 하나에 하나 내린 사랑과 있습니다.',
    createdAt: 1657074329874,
    updatedAt: 1657075309874,
  },
];

const DiscussionDetailPage = () => {
  const [isEditingOpinion, setIsEditingOpinion] = useState(false);
  const {
    data: discussion,
    loading,
    error,
  } = useFetch<TDiscussion.Discussion>('/discussions/1');

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
            <S.Paragraph>{discussion.content}</S.Paragraph>
          </S.DiscussionContentBox>
        </S.DiscussionDetailSection>
        <S.OpinionListSection>
          <S.OpinionCountParagraph>
            {opinions.length}개의 의견
          </S.OpinionCountParagraph>
          <S.DivideLine />
          <S.Table>
            <S.TableRow>
              {opinions.map((opinion) => (
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
          <>
            <TextArea />
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
