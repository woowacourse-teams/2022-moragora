import styled from '@emotion/styled';

const appHeight = window.innerHeight / 16;

export const AppLayout = styled.div`
  position: relative;
  height: ${appHeight - 9}rem;
  padding: 5rem 0 4rem;
  background-color: ${({ theme: { colors } }) => colors['surface']};
`;
