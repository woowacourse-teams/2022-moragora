import { css } from '@emotion/react';
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

export const Tab = styled(NavLink)`
  text-decoration: none;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  z-index: 1;

  &.active {
    color: ${({ theme: { colors } }) => colors['black']};
  }
`;

export const Indicator = styled.div<{ tabPositions: TabPosition[] }>`
  position: absolute;
  bottom: -0.6rem;

  display: flex;
  justify-content: center;

  transition: all 250ms cubic-bezier(0, 0.5, 0.5, 1.1);

  ${({ tabPositions }) =>
    tabPositions.length > 0 &&
    css`
      ${Tab}:nth-child(1).active ~ & {
        left: ${tabPositions[0].left}px;
        width: ${tabPositions[0].width}px;
      }

      ${Tab}:nth-child(2).active ~ & {
        left: ${tabPositions[1].left}px;
        width: ${tabPositions[1].width}px;
      }

      ${Tab}:nth-child(3).active ~ & {
        left: ${tabPositions[2].left}px;
        width: ${tabPositions[2].width}px;
      }
    `};
`;

export const Main = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
`;
