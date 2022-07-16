import styled from '@emotion/styled';

export const Layout = styled.div`
  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const Box = styled.div`
  display: flex;
  flex-wrap: wrap;
`;

export const CoffeeIconImage = styled.img`
  width: 2rem;
  height: 2rem;
`;
