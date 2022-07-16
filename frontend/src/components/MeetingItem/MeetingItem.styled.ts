import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const Layout = styled.div`
  display: inline-block;
  border-radius: 2rem;
  box-shadow: 0px 0px 1rem rgba(0, 0, 0, 0.1);
`;

export const MeetingItemLink = styled(Link)`
  color: inherit;
  text-decoration: inherit;
`;

export const Box = styled.div`
  width: 10rem;
  height: 10rem;
  padding: 1.25rem;

  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

export const MeetingIconImage = styled.img`
  width: 2rem;
  height: 2rem;
`;

export const CheckInSpan = styled.span`
  align-self: flex-end;
`;
