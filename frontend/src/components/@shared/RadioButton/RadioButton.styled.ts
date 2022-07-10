import styled from '@emotion/styled';

export const Input = styled.input`
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;

  width: 1rem;
  height: 1rem;
  position: relative;

  border: 2px solid ${({ theme: { colors } }) => colors['subtle-light']};
  border-radius: 50%;

  transition: 0.1s all linear;

  cursor: pointer;

  :checked {
    border: 4px solid ${({ theme: { colors } }) => colors['primary']};
    outline: unset !important;
  }

  :disabled {
    background-color: ${({ theme: { colors } }) => colors['background']};

    pointer-events: none;

    :checked {
      border: 4px solid ${({ theme: { colors } }) => colors['subtle-light']};
    }
  }
`;
