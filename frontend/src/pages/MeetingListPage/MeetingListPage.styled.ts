import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const Layout = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
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

export const DateBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

export const DateParagraph = styled.p`
  font-size: 1.25rem;
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
  font-size: 1rem;
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
`;

export const CoffeeStackList = styled.ul`
  // reset margin
  margin: 0;
  list-style: none;

  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const EmptyStateBox = styled.div`
  flex: 1;
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
`;

export const MeetingCreateLink = styled(Link)`
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

  opacity: 0;
  animation: dissolve 1.5s;
  animation-delay: 2.7s;
  animation-fill-mode: forwards;

  :hover {
    background-color: ${({ theme: { colors } }) => colors['primary-subtle']};
  }

  :disabled {
    background-color: ${({ theme: { colors } }) => colors['background']};
    color: ${({ theme: { colors } }) => colors['subtle-light']};
    pointer-events: none;
  }
`;

export const EmptyStateImage = styled.img`
  margin-top: -4rem;
  padding-bottom: 2rem;
  width: 10rem;
  height: 10rem;
  opacity: 0;
  animation: smooth-appear 2s;
  animation-fill-mode: forwards;
`;

export const EmptyStateTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 600;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
  opacity: 0;
  animation: smooth-appear 2s;
  animation-fill-mode: forwards;
`;

export const EmptyStateDateParagraph = styled.p`
  box-sizing: border-box;
  margin-top: 0.25rem;
  border-radius: 0.5rem;
  height: 1.5rem;
  width: 10rem;
  animation: skeleton-gradient 1.5s infinite ease-in-out;
`;

export const EmptyStateTimeParagraph = styled.p`
  box-sizing: border-box;
  margin-top: 0.25rem;
  border-radius: 0.5rem;
  height: 1.5rem;
  width: 6rem;
  animation: skeleton-gradient 1.5s infinite ease-in-out;
`;

export const EmptyStateParagraph = styled.p`
  font-size: 1rem;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  opacity: 0;
  animation: dissolve 1s;
  animation-delay: 1.5s;
  animation-fill-mode: forwards;
`;
