import styled from '@emotion/styled';

export const Layout = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  width: 100%;
  padding: 0.75rem 0;
  background-color: ${({ theme: { colors } }) => colors['surface']};
  border: 1px solid ${({ theme: { colors } }) => colors['subtle-light']};
  border-radius: 1rem;

  :hover {
    background-color: ${({ theme: { colors } }) => colors['background']};
  }
`;

export const Icon = styled.img`
  width: 1rem;
  height: 1rem;
`;
