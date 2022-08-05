import styled from '@emotion/styled';

export const Button = styled.button`
  width: 1.5rem;
  height: 1.5rem;
  justify-self: center;
  cursor: pointer;

  color: ${({ theme: { colors } }) => colors['subtle-light']};
  border: 1px solid ${({ theme: { colors } }) => colors['background']};
  border-radius: 0.25rem;

  :active {
    background-color: ${({ theme: { colors } }) => colors['background']};
  }
`;
