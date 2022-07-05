import React from 'react';
import { css } from '@emotion/react';
import Content from '../../components/layouts/Content';
import Footer from '../../components/layouts/Footer';
import * as S from './DiscussionPostPage.styled';

const DiscussionPostPage = () => {
  return (
    <>
      <S.AvatarFigure>
        <S.AvatarImage
          src="http://gravatar.com/avatar/2?d=identicon"
          alt="익명"
        />
        <S.AvatarCaption>익명</S.AvatarCaption>
      </S.AvatarFigure>
      <Content>
        <S.Form>
          <S.Label>
            제목
            <input placeholder="토론할 주제를 적어주세요." />
          </S.Label>
          <S.Label>
            설명
            <textarea placeholder="토론할 주제에 대한 설명을 적어주세요." />
          </S.Label>
        </S.Form>
      </Content>
      <Footer>
        <button
          type="button"
          css={css`
            width: 100%;
            background-color: black;
            color: white;
            padding: 0.5rem;
          `}
        >
          토론 게시
        </button>
      </Footer>
    </>
  );
};

export default DiscussionPostPage;
