import styled from '@emotion/styled';

export const Button = styled.button`
  width: 4rem;
  height: 4rem;
  padding: 1rem;
  border-radius: 50%;
  border: 2px solid ${({ theme: { colors } }) => colors['primary-subtle']};
  color: ${({ theme: { colors } }) => colors['primary-subtle']};

  :hover {
    border-color: ${({ theme: { colors } }) => colors['primary']};
    color: ${({ theme: { colors } }) => colors['primary']};
  }

  :disabled {
    pointer-events: none;

    border-color: ${({ theme: { colors } }) => colors['subtle-light']};
    background-color: ${({ theme: { colors } }) => colors['background']};
    color: ${({ theme: { colors } }) => colors['subtle-light']};
  }
`;

export const IconSVG = styled.svg`
  color: inherit;
`;
