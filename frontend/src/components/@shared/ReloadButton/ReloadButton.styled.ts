import styled from '@emotion/styled';

export const Button = styled.button`
  width: 4rem;
  height: 4rem;
  padding: 1rem;
  border: 2px solid lightgrey;
  border-radius: 50%;

  :hover {
    color: lightgrey;
  }

  :disabled {
    pointer-events: none;

    background-color: #eee;
  }
`;

export const IconSVG = styled.svg`
  color: inherit;
`;
