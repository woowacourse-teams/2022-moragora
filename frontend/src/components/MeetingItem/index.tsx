import * as S from './MeetingItem.styled';
import MeetingIconSVG from '../../assets/meeting.svg';
import React from 'react';
import { Meeting } from 'types/meetingType';

// minute이 60을 넘어갈 때 hour + 1
// 24시를 넘어갈 때 0으로 수정
function reconcileTime(time: string) {
  const timeList = time.split(':');
  let hour = parseInt(timeList[0]);
  let minute = parseInt(timeList[1]);

  if (minute >= 60) {
    hour += 1;
    minute -= 60;
  }

  return `${('0' + (hour % 24)).slice(-2)}:${('0' + minute).slice(-2)}`;
}

// 분에 minute에 해당하는만큼 추가
function addMinute(startTime: string, minute: number) {
  const timeList = startTime.split(':');
  let startHour = timeList[0];
  let startMinute = parseInt(timeList[1]);

  const addedTime = `${startHour}:${('0' + (startMinute + minute)).slice(-2)}`;

  return reconcileTime(addedTime);
}

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
