import styled from '@emotion/styled';

export const AppLayout = styled.div<{ appHeight: number }>`
  position: relative;
  height: ${({ appHeight }) => appHeight / 16 - 9}rem;
  padding: 5rem 0 4rem;
  background-color: ${({ theme: { colors } }) => colors['surface']};
`;
