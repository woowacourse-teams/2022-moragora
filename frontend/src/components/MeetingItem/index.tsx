import React from 'react';
import * as S from './MeetingItem.styled';
import MeetingIconSVG from 'assets/meeting.svg';
import { addMinute } from 'utils/timeUtil';

type MeetingItemProps = {
  meeting: {
    id: 1;
    name: '모임1';
    active: true; // true면 출석 중, false면 출석 준비 중
    startDate: 'yyyy-mm-dd';
    endDate: 'yyyy-mm-dd';
    entranceTime: 'hh:mm';
    closingTime: 'hh:mm'; // 출석 마감 시간
  };
};

const MeetingItem: React.FC<MeetingItemProps> = ({ meeting }) => {
  return (
    <S.Layout>
      <S.MeetingItemLink to={`/meeting/${meeting.id}`}>
        <S.Box>
          <S.MeetingIconImage src={MeetingIconSVG} />
          <span>{meeting.name}</span>
          <span>
            {meeting.entranceTime} ~ {addMinute(meeting.entranceTime, 5)}
          </span>
          <S.CheckInSpan>체크인하세요!</S.CheckInSpan>
        </S.Box>
      </S.MeetingItemLink>
    </S.Layout>
  );
};

export default MeetingItem;
