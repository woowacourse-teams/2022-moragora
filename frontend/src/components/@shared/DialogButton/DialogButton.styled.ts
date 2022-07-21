import styled from '@emotion/styled';
import { css } from '@emotion/react';

type DialogButtonProps = {
  variant: 'confirm' | 'dismiss';
};

export const Button = styled.button<DialogButtonProps>`
  border-radius: 0.75rem;
  padding: 0.75rem 1rem;
  color: ${({ theme: { colors } }) => colors['white']};

  ${({ variant, theme: { colors } }) => {
    if (variant === 'confirm') {
      return css`
        color: ${colors['white']};
        background-color: ${colors['primary']};

        :hover {
          background-color: ${colors['primary-subtle']};
        }
      `;
    }

    return css`
      color: ${colors['normal']};
      background-color: ${colors['transparent']};

      :hover {
        background-color: ${colors['background']};
      }
    `;
  }}

  :disabled {
    color: ${({ theme: { colors } }) => colors['subtle-light']};
    background-color: ${({ theme: { colors } }) => colors['background']};
    pointer-events: none;
  }
`;
