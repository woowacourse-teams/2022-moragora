import styled from '@emotion/styled';

export const Layout = styled.div`
  width: 390px;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 1.5rem 0;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const FieldGroupBox = styled.div`
  display: flex;
  gap: 1rem;
`;

export const FieldBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Label = styled.label`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;
