import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
`;

export const MeetingDetailSection = styled.section`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.75rem;
  border-bottom: 1px solid ${({ theme: { colors } }) => colors['background']};
`;

export const UserListSection = styled.section`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
  width: 100%;
`;

export const UserListBox = styled.div`
  overflow-x: hidden;
  overflow-y: scroll;
  display: flex;
  flex-direction: column;
  flex: 1;
`;

export const Paragraph = styled.p``;

export const UserRowBox = styled.div`
  display: flex;
  justify-content: space-evenly;
  padding: 1rem 0;
`;

export const UserDataBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
`;

export const Form = styled.form`
  display: flex;
  overflow: hidden;
`;
