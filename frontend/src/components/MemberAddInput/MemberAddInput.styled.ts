import styled from '@emotion/styled';

export const Layout = styled.div`
  position: relative;
`;

export const QueryResultList = styled.ul`
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

export const QueryResultListItem = styled.li`
  padding: 0.5rem;

  :hover {
    cursor: pointer;
    background-color: ${({ theme: { colors } }) => colors['surface']};
  }
`;

export const AddedMembersList = styled.ul`
  padding: 0;
  list-style: none;
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
`;

export const AddedMembersListItem = styled.li<{ caption: string }>`
  display: flex;
  position: relative;
  gap: 0.25rem;
  justify-content: center;
  align-items: center;
  border-radius: 40px;
  padding: 0.25rem 0.5rem;
  background-color: ${({ theme: { colors } }) => colors['background']};
  font-weight: 500;
  cursor: default;

  :hover {
    ::after {
      content: '${({ caption }) => caption}';
      position: absolute;
      bottom: -1.5rem;
      padding: 0.25rem;
      background-color: rgb(0, 0, 0, 0.7);
      border-radius: 0.1rem;
      font-size: 0.75rem;
      color: white;
      z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};
    }
  }
`;

export const RemoveButton = styled.button`
  color: ${({ theme: { colors } }) => colors['subtle-light']};
`;
