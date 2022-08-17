import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
  padding: 0.75rem;
`;

export const SectionTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 400;
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

export const EndedCheckTimeSection = styled.section``;

export const CheckTimeSection = styled.section``;

export const MeetingList = styled.ul`
  list-style: none;
  display: flex;
  align-items: center;
  padding: 0;
  gap: 1rem;
  overflow-x: scroll;
  overflow-y: hidden;

  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */

  &::-webkit-scrollbar {
    display: none; /* Chrome, Safari, Opera*/
  }
`;

export const MeetingItem = styled.div`
  display: inline-block;
`;

export const UserListSection = styled.section`
  display: flex;
  flex-direction: column;
  flex: 1;
  width: 100%;
  overflow: hidden;
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

export const UserListBox = styled.div`
  display: flex;
  overflow: hidden;
`;
