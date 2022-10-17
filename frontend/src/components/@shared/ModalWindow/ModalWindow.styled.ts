import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 18rem;
  padding: 1rem;
  border: 2px solid ${({ theme: { colors } }) => colors['primary']};
  border-radius: 0.5rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  z-index: ${({ theme: { zIndex } }) => zIndex['modal']}; ;
`;

export const Title = styled.h2`
  font-size: 1rem;
`;

export const Box = styled.div`
  display: flex;
  align-self: flex-end;
`;

export const ButtonBox = styled.div`
  display: flex;
  gap: 0.5rem;
`;
