import React, { useContext, useEffect } from 'react';
import * as S from './Calendar.styled';
import WeekCell from './WeekCell';
import DateCell from './DateCell';
import AdvancedConfiguration from './AdvancedConfiguration';
import MonthControlBox from './MonthControlBox';
import DivideLine from 'components/@shared/DivideLine';
import Toggle from 'components/@shared/Toggle';
import { CalendarContext } from 'contexts/calendarContext';
import { dateToFormattedString } from 'utils/timeUtil';

const Calendar = () => {
  const {
    events,
    dates,
    initialDate,
    currentDate,
    shouldApplyBeginEndDates,
    clearSelectedDates,
    bindDateCellControlRef,
    clearDateCellControlRef,
    setShouldApplyBeginEndDates,
  } = useContext(CalendarContext);

  useEffect(() => {
    return () => {
      clearDateCellControlRef();
    };
  }, []);

  if (!initialDate) {
    return <S.Layout>상위 요소에 CalendarProvider가 없습니다.</S.Layout>;
  }

  return (
    <S.Layout>
      <S.YearMonthBox>
        <S.YearMonthParagraph>
          {currentDate &&
            `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월`}
        </S.YearMonthParagraph>
        <MonthControlBox />
      </S.YearMonthBox>
      <DivideLine />
      <button
        type="button"
        onClick={() => {
          clearSelectedDates();
        }}
      >
        선택 해제
      </button>
      <Toggle
        defaultChecked={shouldApplyBeginEndDates}
        onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
          setShouldApplyBeginEndDates(e.target.checked);
        }}
      >
        요일 적용 기간 설정
      </Toggle>
      <AdvancedConfiguration />
      <DivideLine />
      <S.CalendarBox>
        <WeekCell day={0}>일</WeekCell>
        <WeekCell day={1}>월</WeekCell>
        <WeekCell day={2}>화</WeekCell>
        <WeekCell day={3}>수</WeekCell>
        <WeekCell day={4}>목</WeekCell>
        <WeekCell day={5}>금</WeekCell>
        <WeekCell day={6}>토</WeekCell>
        {Array.from({ length: dates[0].getDay() }).map((_, index) => (
          <DateCell key={index} disabled />
        ))}
        {dates.map((date) => (
          <DateCell
            ref={bindDateCellControlRef(date)}
            key={dates[0].getDay() + date.getDate()}
            date={date}
            event={events.find(
              (event) => event.date === dateToFormattedString(date)
            )}
          />
        ))}
      </S.CalendarBox>
    </S.Layout>
  );
};

export default Calendar;
