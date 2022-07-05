import React from 'react';
import { css } from '@emotion/react';
import Discussion from '../../components/Discussion';
import Content from '../../components/layouts/Content';
import Footer from '../../components/layouts/Footer';
import TableRow from './DiscussionListPage.styled';

const DiscussionListPage = () => {
  return (
    <>
      <Content>
        <table>
          <TableRow>
            {Array.from({ length: 16 }).map((discussion) => (
              <td>
                <Discussion />
                <hr
                  css={css`
                    margin: 0;
                    border-top: 1px solid lightgray;
                  `}
                />
              </td>
            ))}
          </TableRow>
        </table>
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
          의견 작성
        </button>
      </Footer>
    </>
  );
};

export default DiscussionListPage;
