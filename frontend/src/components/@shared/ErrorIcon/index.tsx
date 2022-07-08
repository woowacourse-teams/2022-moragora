import React from 'react';
import { css } from '@emotion/react';
import * as S from './ErrorIcon.styled';

const ErrorIcon = () => {
  return (
    <div>
      <S.Title
        css={css`
          font-family: monospace;
        `}
      >
        ( ˃̣̣̥⌓˂̣̣̥)
      </S.Title>
      <S.Paragraph
        css={css`
          font-family: monospace;
          text-align: center;
        `}
      >
        에러가 났다고라...
      </S.Paragraph>
    </div>
  );
};

export default ErrorIcon;
