import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const LayoutLink = styled(Link)<{ disabled?: boolean }>`
  padding: 0.75rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  font-size: 1.25rem;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
  text-decoration: none;

  & svg {
    width: 1rem;
    height: 1rem;
  }

  ${({ disabled }) =>
    disabled &&
    css`
      pointer-events: none;
    `}
`;

export const ContentBox = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;

export const RightChevronIconSVG = styled.svg`
  justify-self: flex-end;
`;
