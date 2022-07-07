import React, { useEffect, useState } from 'react';
import { css } from '@emotion/react';
import DiscussionItem from '../../components/DiscussionItem';
import Content from '../../components/layouts/Content';
import Footer from '../../components/layouts/Footer';
import TableRow from './DiscussionListPage.styled';
import Button from '../../components/@shared/Button';
import * as T from '../../types/DiscussionTypes';
import useFetch from '../../hooks/useFetch';

const DiscussionListPage = () => {
  const { data, loading, error } = useFetch<{
    discussions: T.Discussion[];
  }>('/discussions');

  if (loading) {
    return <>Loading...</>;
  }

  if (error) {
    return <>Error...</>;
  }

  return (
    <>
      <Content>
        <table>
          <TableRow>
            {data.discussions.map((discussion) => (
              <td key={discussion.id}>
                <DiscussionItem discussion={discussion} />
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
