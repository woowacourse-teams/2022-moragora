import styled from '@emotion/styled';

export const Label = styled.label`
  display: flex;
  align-items: center;
  gap: 0.5em;
  width: fit-content;
  height: fit-content;
`;

export const Input = styled.input`
  -webkit-appearance: none;
  appearance: none;
  background-color: ${({ theme: { colors } }) => colors['background']};
  margin: 0;

  position: relative;
  font: inherit;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  width: 2rem;
  height: 1rem;

  border-radius: 0.5rem;

  display: grid;
  place-content: center;

  transition: all 0.15s cubic-bezier(0.4, 0, 0.2, 1);

  :checked {
    color: ${({ theme: { colors } }) => colors['primary']};
    background-color: currentColor;
  }

  ::before {
    content: '';
    position: absolute;
    width: 1rem;
    height: 1rem;
    box-sizing: border-box;
    border: 1px solid ${({ theme: { colors } }) => colors['subtle-light']};
    border-radius: 50%;
    background-color: ${({ theme: { colors } }) => colors['white']};
  }

  :checked::before {
    transform: translateX(100%);
    color: ${({ theme: { colors } }) => colors['white']};
  }

  :disabled {
    color: ${({ theme: { colors } }) => colors['subtle-light']};
    background-color: ${({ theme: { colors } }) => colors['background']};
  }

  :disabled:checked {
    background-color: ${({ theme: { colors } }) => colors['subtle-light']};
  }
`;

export const SwitchHeadBox = styled.div``;
