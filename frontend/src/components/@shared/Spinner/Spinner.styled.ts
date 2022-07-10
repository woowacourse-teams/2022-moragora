import styled from '@emotion/styled';

export const SpinnerBox = styled.div`
  width: 4rem;
  height: 4rem;

  fill: ${({ theme: { colors } }) => colors['primary-subtle']};
  color: ${({ theme: { colors } }) => colors['transparent']};
`;

export const SpinnerSVG = styled.svg`
  fill: inherit;

  animation: spin 1s linear infinite;

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
`;
