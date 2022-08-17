import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import DialogButton from 'components/@shared/DialogButton';
import { Link } from 'react-router-dom';

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

export const MeetingDetailBox = styled.div`
  display: flex;
  flex-direction: column;
  overflow: hidden;
`;

export const MeetingStatusSection = styled.section`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.75rem;
`;

export const SectionTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 400;
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
  width: 100%;
  overflow-y: hidden;
`;

export const UserListBox = styled.div`
  display: flex;
  overflow-y: hidden;
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

export const EmptyStateBox = styled.div`
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
`;

export const EmptyStateTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 600;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
`;

export const EmptyStateParagraph = styled.p`
  font-size: 1rem;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
`;

export const EventCreateLink = styled(Link)`
  // reset
  color: inherit;
  text-decoration: inherit;

  margin-top: 1.5rem;
  text-align: center;
  width: 100%;
  background-color: ${({ theme: { colors } }) => colors['primary']};
  color: ${({ theme: { colors } }) => colors['white']};
  padding: 0.75rem 0;
  border-radius: 1.5rem;

  :hover {
    background-color: ${({ theme: { colors } }) => colors['primary-subtle']};
  }

  :disabled {
    background-color: ${({ theme: { colors } }) => colors['background']};
    color: ${({ theme: { colors } }) => colors['subtle-light']};
    pointer-events: none;
  }
`;
