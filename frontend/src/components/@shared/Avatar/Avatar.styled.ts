import styled from '@emotion/styled';

export const Layout = styled.div`
  width: 3rem;
  height: 3rem;
  border: 2px solid;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const Image = styled.img`
  width: 5rem;
  height: 5rem;
`;
