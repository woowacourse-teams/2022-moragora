import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
`;

export const DiscussionDetailSection = styled.section`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.75rem;
  border-bottom: 1px solid lightgray;
`;

export const DiscussionTitle = styled.h2`
  margin: 0;
`;

export const DiscussionInformationBox = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Paragraph = styled.p`
  margin: 0;
`;

export const TimestampSpan = styled.span`
  margin-left: 0.5rem;
`;

export const ControlButtonBox = styled.div`
  display: flex;
  gap: 0.5rem;
`;

export const ControlButton = styled.button``;

export const DiscussionContentBox = styled.div``;

export const OpinionListSection = styled.section`
  overflow-x: hidden;
  overflow-y: scroll;
  width: 100%;
`;

export const OpinionCountParagraph = styled.p`
  padding: 0 0.75rem;
`;

export const OpinionEditorButtonBox = styled.div`
  display: flex;
  gap: 0.5rem;
  align-self: flex-end;
`;

export const Table = styled.table`
  border-spacing: 0;
`;

export const TableRow = styled.tr`
  display: flex;
  flex-direction: column;
`;

export const TabelData = styled.td`
  padding: 0;
`;
export const DivideLine = styled.hr`
  width: 100%;
  margin: 0;
  border-top: 1px solid lightgray;
`;
