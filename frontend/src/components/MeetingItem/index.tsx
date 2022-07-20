import React from 'react';
import * as S from './MeetingItem.styled';
import MeetingIconSVG from 'assets/meeting.svg';
import { addMinute } from 'utils/timeUtil';
import { MeetingWithTardyCount } from 'types/meetingType';

type MeetingItemProps = {
  meeting: MeetingWithTardyCount;
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
