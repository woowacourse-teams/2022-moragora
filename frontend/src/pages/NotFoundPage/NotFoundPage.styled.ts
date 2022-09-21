import styled from '@emotion/styled';

export const Layout = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  overflow: hidden;
`;

export const IconBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
`;

export const Title = styled.h2`
  font-size: 4rem;
`;

export const Paragraph = styled.p`
  font-size: 1.5rem;
  margin-top: 3rem;
`;
