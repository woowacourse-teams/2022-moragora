import * as S from './MeetingItem.styled';
import MeetingIconSVG from '../../assets/meeting.svg';
import React from 'react';

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
    <S.Layout>
      <S.MeetingIconImage src={MeetingIconSVG} />
      <span>{meeting.name}</span>
      <span>
        {meeting.entranceTime} ~ {addMinute(meeting.entranceTime, 5)}
      </span>
      <S.CheckInSpan>체크인하세요!</S.CheckInSpan>
    </S.Layout>
  );
};

export default MeetingItem;
