import React from 'react';
import Content from '../../components/layouts/Content';
import Footer from '../../components/layouts/Footer';
import * as S from './DiscussionPostPage.styled';
import Button from '../../components/@shared/Button';
import * as T from '../../types/DiscussionTypes';

const postData = (url: string, payload: T.DiscussionsRequestBody) => {
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const DiscussionPostPage = () => {
  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const payload = Object.fromEntries(
      formData.entries()
    ) as T.DiscussionsRequestBody;

    postData('/discussions', payload);
  };

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
        <S.Form id="discussion-form" onSubmit={handleSubmit}>
          <S.Label>
            제목
            <input
              name="title"
              placeholder="토론할 주제를 적어주세요."
              required
            />
          </S.Label>
          <S.Label>
            설명
            <textarea
              name="content"
              placeholder="토론할 주제에 대한 설명을 적어주세요."
              required
            />
          </S.Label>
        </S.Form>
      </Content>
      <Footer>
        <Button type="submit" form="discussion-form">
          토론 게시
        </Button>
      </Footer>
    </>
  );
};

export default DiscussionPostPage;
