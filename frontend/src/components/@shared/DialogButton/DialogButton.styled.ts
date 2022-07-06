import React from 'react';
import { css } from '@emotion/react';
import styled from '@emotion/styled';

type DialogButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant: 'confirm' | 'dismiss';
};

const stylesFromVariant = (props: DialogButtonProps) => {
  if (props.variant === 'confirm') {
    return css`
      background-color: black;
      color: white;

      :hover {
        background-color: gray;
      }
    `;
  }

  return css`
    background-color: transparent;
    color: black;

    :hover {
      background-color: lightgray;
    }
  `;
};

export const Button = styled.button<DialogButtonProps>`
  ${stylesFromVariant}

  border-radius: 0.75rem;
  padding: 0.75rem 1rem;

  :disabled {
    background-color: lightgray;
    color: gray;
    pointer-events: none;
  }
`;

export const temp = null;
