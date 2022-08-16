import styled from '@emotion/styled';
import { NavLink } from 'react-router-dom';

type TabPosition = DOMRect;

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 2rem;
  overflow: scroll;
`;

export const TabNav = styled.nav`
  position: relative;
  display: flex;
  justify-content: space-around;
  font-weight: 600;
`;

export const TabNavLink = styled(NavLink)`
  text-decoration: none;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  z-index: 1;

  &.active {
    color: ${({ theme: { colors } }) => colors['black']};
  }
`;

const setTabPosition = (tabPositions: TabPosition[]) => {
  let styles: Record<string, { left: string; width: string }> = {};

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

  transition: all 300ms linear;

  ${({ tabPositions }) => setTabPosition(tabPositions)}
`;

export const MainBox = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
`;
