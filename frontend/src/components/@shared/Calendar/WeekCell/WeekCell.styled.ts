import styled from '@emotion/styled';

export const Button = styled.button`
  width: 100%;
  height: 100%;
  justify-self: center;
  cursor: pointer;

  color: ${({ theme: { colors } }) => colors['subtle-light']};
  border: 1px solid ${({ theme: { colors } }) => colors['background']};
  border-radius: 0.25rem;

  :active {
    background-color: ${({ theme: { colors } }) => colors['background']};
  }
`;
