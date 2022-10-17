import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  justify-content: space-between;
  border-radius: 0.75rem;
  padding: 0.75rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const DescriptionBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

export const ButtonBox = styled.div``;

export const Button = styled.button`
  padding: 0.75rem;
  border-radius: 0.75rem;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};

  &:hover {
    background-color: ${({ theme: { colors } }) => colors['surface']};
  }
`;
