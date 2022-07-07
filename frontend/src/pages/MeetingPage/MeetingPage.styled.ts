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
  overflow-x: hidden;
  overflow-y: scroll;
  width: 100%;
`;

export const Paragraph = styled.p`
  margin: 0;
`;

export const Table = styled.table`
  border-spacing: 0;
`;

export const TableRow = styled.tr``;

export const TabelData = styled.td`
  padding: 0;
`;

export const DivideLine = styled.hr`
  width: 100%;
  margin: 0;
  border-top: 1px solid lightgray;
`;
