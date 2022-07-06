import React from 'react';
import { css } from '@emotion/react';
import DiscussionItem from '../../components/DiscussionItem';
import Content from '../../components/layouts/Content';
import Footer from '../../components/layouts/Footer';
import TableRow from './DiscussionListPage.styled';
import Button from '../../components/@shared/Button';
import * as T from '../../types/DiscussionTypes';

const discussion: Omit<T.Discussion, 'id'> = {
  title: 'Title',
  content: 'content',
  views: 1,
  createdAt: 1657080703969,
  updatedAt: null,
};

const discussions: T.Discussion[] = Array.from({ length: 16 }).map((_, id) => ({
  ...discussion,
  id,
}));

const DiscussionListPage = () => {
  return (
    <>
      <Content>
        <table>
          <TableRow>
            {discussions.map((discussionItem) => (
              <td key={discussionItem.id}>
                <DiscussionItem discussion={discussionItem} />
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
