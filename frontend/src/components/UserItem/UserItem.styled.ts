import styled from '@emotion/styled';

export const Layout = styled.li`
  padding: 0.5rem 0.75rem;
  display: flex;
  gap: 0.5rem;
  justify-content: space-between;
  align-items: center;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const Box = styled.div`
  display: flex;
  gap: 0.5rem;
`;

export const AttendanceStatusText = styled.span`
  color: ${({ theme: { colors } }) => colors['primary']};
`;

export const CoffeeIconImageBox = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
`;

export const CoffeeIconImage = styled.img`
  width: 2rem;
  height: 2rem;
`;
