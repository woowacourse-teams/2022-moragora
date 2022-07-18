import styled from '@emotion/styled';

export const Input = styled.input`
  padding: 0.75rem;
  border: 1px solid ${({ theme: { colors } }) => colors['subtle-light']};
  border-radius: 0.5rem;
  width: 100%;

  :focus {
    outline-color: 2px solid ${({ theme: { colors } }) => colors['primary']};
  }

  ::placeholder {
    color: ${({ theme: { colors } }) => colors['subtle-light']};
  }

  :disabled {
    background-color: ${({ theme: { colors } }) => colors['background']};
  }
`;
