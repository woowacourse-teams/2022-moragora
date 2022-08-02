import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import DialogButton from 'components/@shared/DialogButton';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
`;

export const SpinnerBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ErrorBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 2rem;
`;

export const TitleSection = styled.section`
  padding: 0.75rem;
`;

export const MeetingDetailSection = styled.section`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.75rem;
`;

export const SectionTitle = styled.h2`
  font-size: 1.5rem;
  font-weight: 500;
`;

export const ProgressBox = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
`;

const shadow = keyframes`
  from {
    box-shadow: none
  }to{
    box-shadow: 0 0 10px orange;
  }
`;

export const EmptyButton = styled(DialogButton)`
  animation: ${shadow} 0.7s ease-out;
  animation-delay: 1s;
  animation-fill-mode: forwards;
`;

export const StackDetailBox = styled.div`
  flex-basis: 23%;
  padding-bottom: 1rem;
`;

export const UserListSectionHeader = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0.75rem;
  align-items: flex-end;
`;

export const UserListSection = styled.section`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
  width: 100%;
`;

export const UserList = styled.ul`
  // reset margin
  margin: 0;

  width: 100%;
  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow-x: hidden;
  overflow-y: scroll;
`;

export const Paragraph = styled.p``;

export const UserDataBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
`;

export const UserListBox = styled.div`
  display: flex;
  overflow: hidden;
`;
