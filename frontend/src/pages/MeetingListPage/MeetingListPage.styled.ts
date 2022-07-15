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

export const TimeSection = styled.section`
  // global padding
  padding: 0.75rem;

  display: flex;
`;

export const MeetingListSection = styled.section``;

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

export const MeetingItem = styled.li`
  min-width: 160px;
  height: 160px;
  padding: 1.25rem;
  border-radius: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  box-shadow: 0px 0px 1rem rgba(0, 0, 0, 0.1);
`;

export const MeetingIconImage = styled.img`
  width: 2rem;
  height: 2rem;
`;

export const MeetingNameSpan = styled.span``;

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
