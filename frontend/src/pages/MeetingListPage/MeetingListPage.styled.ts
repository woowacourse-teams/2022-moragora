import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
`;

export const Title = styled.h2`
  // global padding
  padding: 0.75rem;
`;

export const MeetingList = styled.ul`
  // reset
  list-style: none;
  margin: 0;
  padding: 0;

  // global padding
  padding: 0.75rem;

  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 1rem;
  overflow: scroll;
`;

export const MeetingItem = styled.li`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
`;

export const MeetingIconSVG = styled.svg`
  width: 1.5rem;
  height: 1.5rem;
  color: ${({ theme: { colors } }) => colors['primary-subtle']};
`;

export const MeetingNameSpan = styled.span``;
