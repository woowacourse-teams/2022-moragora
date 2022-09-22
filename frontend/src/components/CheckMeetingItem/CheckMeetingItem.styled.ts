import styled from '@emotion/styled';

export const Layout = styled.li<{ clicked: boolean }>`
  display: flex;
  align-items: center;
  min-width: max-content;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  font-size: 1rem;

  color: ${({ theme: { colors }, clicked }) =>
    clicked ? colors['white'] : colors['black']};

  background-color: ${({ theme: { colors }, clicked }) =>
    clicked ? colors['primary'] : colors['white']};
  border-radius: 1rem;

  cursor: pointer;

  &:hover {
    background-color: ${({ theme: { colors } }) => colors['primary-subtle']};
  }
`;
