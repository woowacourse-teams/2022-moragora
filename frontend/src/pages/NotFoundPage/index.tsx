import * as S from './NotFoundPage.styled';
import Footer from 'components/layouts/Footer';

const NotFoundPage = () => {
  return (
    <>
      <S.Layout>
        <S.IconBox>
          <S.Title>( ˃̣̣̥⌓˂̣̣̥)</S.Title>
          <S.Paragraph>페이지를 찾을 수 없습니다..</S.Paragraph>
        </S.IconBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default NotFoundPage;
