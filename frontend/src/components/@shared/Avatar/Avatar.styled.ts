import styled from '@emotion/styled';

export const Layout = styled.div`
  width: 2.5rem;
  height: 2.5rem;
  border: 2px solid;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const Image = styled.img`
  width: 4rem;
  height: 4rem;
`;
