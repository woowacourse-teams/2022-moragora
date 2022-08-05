import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  font-family: monospace;
`;

export const Title = styled.h2`
  font-size: 4rem;
`;

export const Paragraph = styled.p`
  font-size: 1.5rem;
  margin-top: 4rem;
`;
