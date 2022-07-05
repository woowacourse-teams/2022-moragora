import { css } from '@emotion/react';
import Header from '../Header';
import Footer from '../Footer';
import Content from '../Content';
import Layout from './MobileLayout.styled';

const MobileLayout = () => {
  return (
    <Layout>
      <Header />

      <Content>Content here</Content>

      <Footer>
        <textarea
          css={css`
            width: 100%;
          `}
        />
        <button
          type="button"
          css={css`
            width: 100%;
            background-color: black;
            color: white;
            padding: 0.5rem;
          `}
        >
          의견 작성
        </button>
      </Footer>
    </Layout>
  );
};

export default MobileLayout;
