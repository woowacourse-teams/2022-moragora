import styled from '@emotion/styled';

export const TextArea = styled.textarea`
  height: 6rem;
  resize: none;
  padding: 0.75rem;
  border: 1px solid ${({ theme: { colors } }) => colors['subtle-light']};
  border-radius: 0.5rem;

  ::placeholder {
    color: ${({ theme: { colors } }) => colors['subtle-light']};
  }

  :focus {
    outline: 2px solid ${({ theme: { colors } }) => colors['primary']};
  }

  :disabled {
    background-color: ${({ theme: { colors } }) => colors['background']};
  }
`;
