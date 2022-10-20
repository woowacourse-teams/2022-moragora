import styled from '@emotion/styled';
import { NavLink } from 'react-router-dom';

export const Nav = styled.nav`
  position: fixed;
  height: 4rem;
  right: 0;
  bottom: 0;
  left: 0;
  display: flex;
  align-items: center;
  border-top: 1px solid ${({ theme: { colors } }) => colors['background']};
  padding: 0 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  z-index: ${({ theme: { zIndex } }) => zIndex['footer']};
`;

export const MenuNavLink = styled(NavLink)`
  text-decoration: inherit;
  flex: 1;
  color: inherit;

  &.active {
    color: ${({ theme: { colors } }) => colors['primary']};
  }
`;

export const Figure = styled.figure`
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
`;

export const Figcaption = styled.figcaption`
  font-size: 0.75rem;
  width: ;
`;
