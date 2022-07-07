import styled from '@emotion/styled';

export const SpinnerBox = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const SpinnerSVG = styled.svg`
  width: 2rem;
  height: 2rem;
  fill: gray;
  color: transparent;

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
