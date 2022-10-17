import styled from '@emotion/styled';
import DialogButton from 'components/@shared/DialogButton';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  width: 18rem;
  padding: 1rem;
  gap: 1rem;
  border: 2px solid ${({ theme: { colors } }) => colors['primary']};
  border-radius: 0.5rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  z-index: ${({ theme: { zIndex } }) => zIndex['modal']};
`;

export const StatsBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  overflow: scroll;
`;

export const Title = styled.h2`
  font-size: 1rem;
`;

export const ButtonBox = styled.div`
  display: flex;
  gap: 1.5rem;
`;

export const RowBox = styled.div`
  padding: 0 1rem;
  display: grid;
  grid: '. .';
  /* justify-content: space-between; */
  align-items: center;
  column-gap: 1rem;
  font-size: 1rem;
  font-weight: 600;
`;

export const CoffeeIconBox = styled.div`
  display: flex;
  align-items: center;
`;

export const Box = styled.div`
  display: flex;
  align-self: flex-end;
`;
