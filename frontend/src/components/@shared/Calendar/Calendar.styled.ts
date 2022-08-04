import styled from '@emotion/styled';

export const Layout = styled.div`
  width: 390px;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 1.5rem 0;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const YearMonthBox = styled.div`
  display: flex;
  justify-content: space-evenly;
`;

export const YearMonthParagraph = styled.p`
  width: 184px;
  text-align: center;
  font-weight: 700;
  letter-spacing: 1%;
  font-size: 14px;
`;

export const CalendarBox = styled.div`
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-template-rows: 1.5rem;
  grid-row-gap: 0.75rem;
`;

export const DateBox = styled.div`
  height: 1.5rem;
  border: 1px solid;
  display: flex;
  justify-content: center;
  align-items: center;
`;
