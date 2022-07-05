import React from 'react';
import { css } from '@emotion/react';
import Discussion from '../../components/Discussion';
import Content from '../../components/layouts/Content';
import Footer from '../../components/layouts/Footer';
import TableRow from './DiscussionListPage.styled';
import Button from '../../components/@shared/Button';

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
        <Button>새로운 토론 작성</Button>
      </Footer>
    </>
  );
};

export default DiscussionListPage;
