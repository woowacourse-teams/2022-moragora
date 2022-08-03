import styled from '@emotion/styled';
import DialogButton from 'components/@shared/DialogButton';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 316px;
  padding: 36px 0;
  gap: 4rem;
  border: 4px solid ${({ theme: { colors } }) => colors['primary']};
  border-radius: 0.5rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  z-index: 50;
`;

export const StatsBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

export const Header = styled.h1`
  font-size: 1.5rem;
`;

export const ButtonBox = styled.div`
  display: flex;
  gap: 1.5rem;
`;

export const RowBox = styled.div`
  display: grid;
  grid: '. .';
  justify-content: space-between;
  align-items: center;
  column-gap: 1rem;
  font-size: 2rem;
  font-weight: 600;
`;

export const CoffeeIconBox = styled.div`
  display: flex;
  align-items: center;
`;

export const CloseButton = styled(DialogButton)`
  width: 6rem;
`;

export const dismissButton = styled(DialogButton)`
  background-color: ${({ theme }) => theme.colors['primary-subtle']};
  color: ${({ theme }) => theme.colors['subtle-dark']};
`;
