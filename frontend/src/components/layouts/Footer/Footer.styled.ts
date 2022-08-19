import styled from '@emotion/styled';
import { NavLink } from 'react-router-dom';

export const Nav = styled.nav`
  height: 4rem;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  border-top: 1px solid ${({ theme: { colors } }) => colors['background']};
  padding: 0 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const MenuNavLink = styled(NavLink)`
  text-decoration: inherit;

  color: inherit;

  &.active {
    color: ${({ theme: { colors } }) => colors['primary']};
  }
`;

export const Figure = styled.figure`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
`;

export const Figcaption = styled.figcaption`
  font-size: 0.75rem;
`;
