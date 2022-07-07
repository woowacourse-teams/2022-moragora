import { css } from '@emotion/react';
import * as S from './ErrorIcon.styled';

const ErrorIcon = () => {
  return (
    <S.Paragraph
      css={css`
        font-family: monospace;
      `}
    >
      /_\
    </S.Paragraph>
  );
};

export default ErrorIcon;
