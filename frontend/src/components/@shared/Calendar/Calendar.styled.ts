import styled from '@emotion/styled';

export const Layout = styled.div`
  width: fit-content;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.75rem 0;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const YearMonthBox = styled.div`
  display: flex;
  justify-content: space-evenly;
`;

export const YearMonthParagraph = styled.p`
  text-align: center;
  font-weight: 700;
  letter-spacing: 1%;
  font-size: 1rem;
`;

export const CalendarBox = styled.div`
  padding: 0 0.75rem;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-row-gap: 0.5rem;
  grid-column-gap: 0.5rem;
`;
