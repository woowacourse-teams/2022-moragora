import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { MeetingEvent } from 'types/eventType';

export const Layout = styled.div<{
  event?: MeetingEvent;
  disabled?: boolean;
  isSelected?: boolean;
}>`
  position: relative;
  min-width: 1.5rem;
  height: 1.5rem;
  display: flex;
  justify-self: center;
  justify-content: center;
  align-items: center;
  cursor: pointer;

  transition: background-color 0.2s, color 0.2s;

  ${({ isSelected, theme: { colors } }) =>
    isSelected &&
    css`
      outline: 2px solid ${colors['primary']};
    `}

  ${({ event, theme: { colors } }) =>
    event &&
    css`
      color: ${colors['white']};
      background-color: ${colors['primary-subtle']};
    `}

  ${({ disabled, theme: { colors } }) =>
    disabled &&
    css`
      pointer-events: none;
      color: ${colors['subtle-light']};
    `}

  &.highlight {
    color: ${({ theme: { colors } }) => colors['white']};
    background-color: ${({ theme: { colors } }) => colors['primary']};
  }

  :hover {
    ::after {
      content: ${({ event }) =>
        event ? `'${event.meetingStartTime} - ${event.meetingEndTime}'` : ''};
      position: absolute;
      bottom: -1.5rem;
      padding: 0.25rem;
      width: 5rem;
      text-align: center;
      background-color: rgb(0, 0, 0, 0.7);
      border-radius: 0.1rem;
      font-size: 0.75rem;
      color: white;
      z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};
    }
  }
`;
