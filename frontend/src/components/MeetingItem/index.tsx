import * as S from './MeetingItem.styled';
import MeetingIconSVG from '../../assets/meeting.svg';
import React from 'react';

type Meeting = {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  entranceTime: string;
  leaveTime: string;
  attendanceCount: number;
};

type MeetingItemProps = {
  meeting: Meeting;
};

const MeetingItem: React.FC<MeetingItemProps> = ({ meeting }) => {
  return (
    <S.MeetingItem>
      <S.MeetingIconImage src={MeetingIconSVG} />
      <span>{meeting.name}</span>
      <span>10:00 ~ 10:05</span>
      <S.CheckInSpan>체크인하세요!</S.CheckInSpan>
    </S.MeetingItem>
  );
};

export default MeetingItem;
