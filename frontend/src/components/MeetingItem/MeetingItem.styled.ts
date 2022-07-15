import styled from '@emotion/styled';

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

export const CheckInSpan = styled.span`
  align-self: flex-end;
`;
