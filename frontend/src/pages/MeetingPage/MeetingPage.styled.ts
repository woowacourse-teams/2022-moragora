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
  border-bottom: 1px solid lightgray;
`;

export const MeetingTitle = styled.h2`
  margin: 0;
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
  gap: 1rem;
`;

export const Paragraph = styled.p`
  margin: 0;
`;

export const UserRowBox = styled.div`
  display: flex;
  justify-content: space-evenly;
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

export const RadioButton = styled.input`
  width: 1rem;
  height: 1rem;
`;

export const DivideLine = styled.hr`
  width: 100%;
  margin: 0;
  border-top: 1px solid lightgray;
`;
