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

export const UserItem = styled.li`
  padding: 0.75rem;
  display: flex;
  gap: 0.5rem;
  justify-content: space-between;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const CoffeeIconImageBox = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
`;

export const CoffeeIconImage = styled.img`
  width: 2rem;
  height: 2rem;
`;

// export const UserListBox = styled.div`
//   overflow-x: hidden;
//   overflow-y: scroll;
//   display: flex;
//   flex-direction: column;
//   flex: 1;
// `;

export const Paragraph = styled.p``;

// export const UserRowBox = styled.div`
//   display: flex;
//   justify-content: space-evenly;
//   padding: 1rem 0;
// `;

export const UserDataBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
`;

export const Form = styled.form`
  display: flex;
  overflow: hidden;
`;
