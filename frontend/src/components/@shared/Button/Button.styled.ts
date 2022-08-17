import styled from '@emotion/styled';

const StyledButton = styled.button`
  width: 100%;
  background-color: ${({ theme: { colors } }) => colors['primary']};
  color: ${({ theme: { colors } }) => colors['white']};
  padding: 0.75rem 0;
  border-radius: 1rem;

  :hover {
    background-color: ${({ theme: { colors } }) => colors['primary-subtle']};
  }

  :disabled {
    background-color: ${({ theme: { colors } }) => colors['background']};
    color: ${({ theme: { colors } }) => colors['subtle-light']};
    pointer-events: none;
  }
`;

export default StyledButton;
