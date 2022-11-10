import styled from '@emotion/styled';
import { NavLink } from 'react-router-dom';

type TabPosition = DOMRect;

export const Layout = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  overflow-y: hidden;
`;

export const SpinnerBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ErrorBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 2rem;
`;

export const TabNavBox = styled.div`
  position: fixed;
  width: 100%;
  height: 3rem;
  background-color: ${({ theme: { colors } }) => colors['surface']};
  box-shadow: 0 0.1rem 0.1rem ${({ theme: { colors } }) => colors['background']};
  z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};
`;

export const TabNav = styled.nav`
  position: relative;
  display: flex;
  justify-content: space-evenly;
  font-weight: 600;
`;

export const TabNavLink = styled(NavLink)`
  text-decoration: none;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};

  transition: all 300ms ease-out;

  &.active {
    color: ${({ theme: { colors } }) => colors['black']};
  }
`;

const setTabPosition = (tabPositions: TabPosition[]) => {
  const styles: Record<string, { left: string; width: string }> = {};

  tabPositions.forEach((position, index) => {
    styles[`${TabNavLink}:nth-of-type(${index + 1}).active ~ &`] = {
      left: `${position.left / 16}rem`,
      width: `${position.width / 16}rem`,
    };
  });

  return styles;
};

export const IndicatorBox = styled.div<{ tabPositions: TabPosition[] }>`
  position: absolute;
  bottom: -0.6rem;

  display: flex;
  justify-content: center;

  transition: all 300ms ease-out;

  ${({ tabPositions }) => setTabPosition(tabPositions)}
`;

export const MainBox = styled.div`
  display: flex;
  flex: 1;
  padding-top: 3rem;
  overflow-y: scroll;
`;
