import styled from '@emotion/styled';

export const Layout = styled.section``;

export const Label = styled.label`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  overflow: hidden;
`;

export const MasterAssignBox = styled.div``;

export const SelectBox = styled.div`
  padding: 0.75rem 1.5rem;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  display: flex;
  gap: 0.5rem;
`;

export const SelectSpan = styled.span`
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: ${({ theme: { colors } }) => colors['primary']};
  cursor: pointer;
`;

export const ParticipantList = styled.ul`
  padding: 0;
  list-style: none;
  position: absolute;
  width: 100%;
  max-height: 12rem;
  z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};

  display: flex;
  flex-direction: column;
  margin-top: 0.25rem;

  border-radius: 0.5rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  box-shadow: 0 0 1rem rgba(0, 0, 0, 0.1);

  overflow: scroll;
`;

export const ParticipantListItem = styled.li`
  padding: 0.5rem;

  :hover {
    cursor: pointer;
    background-color: ${({ theme: { colors } }) => colors['surface']};
  }
`;
