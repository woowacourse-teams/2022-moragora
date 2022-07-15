import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
`;

export const DateBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

export const DateParagraph = styled.p`
  font-size: 1.5rem;
`;

export const TimeSection = styled.section`
  // global padding
  padding: 0.75rem;

  display: flex;
`;

export const MeetingListSection = styled.section``;

export const MeetingItemLink = styled(Link)`
  color: inherit;
  text-decoration: inherit;
`;

export const TitleBox = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  // global padding
  padding: 0.75rem;
`;

export const Title = styled.h2`
  font-size: 1.25rem;
  font-weight: 300;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
`;

export const PageLink = styled(Link)`
  font-size: 0.75rem;
  text-decoration: none;
  color: ${({ theme: { colors } }) => colors['primary']};
`;

export const MeetingList = styled.ul`
  // reset
  list-style: none;
  margin: 0;
  padding: 0;

  // global padding
  padding: 0.75rem;

  display: flex;
  gap: 1rem;
  overflow: scroll;
`;

export const CoffeeStackSection = styled.section`
  display: flex;
  flex-direction: column;
  overflow: hidden;
`;

export const CoffeeStackList = styled.ul`
  // reset margin
  margin: 0;

  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow-x: hidden;
  overflow-y: scroll;
`;

export const CoffeeStackItem = styled.li`
  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const CoffeeIconImageBox = styled.div`
  display: flex;
  flex-wrap: wrap;
`;

export const CoffeeIconImage = styled.img`
  width: 2rem;
  height: 2rem;
`;
