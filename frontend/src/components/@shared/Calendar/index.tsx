import React, { useContext, useEffect } from 'react';
import Toggle from 'components/@shared/Toggle';
import { CalendarContext } from 'contexts/calendarContext';
import { dateToFormattedString } from 'utils/timeUtil';
import { css } from '@emotion/react';
import * as S from './Calendar.styled';
import WeekCell from './WeekCell';
import DateCell from './DateCell';
import AdvancedConfiguration from './AdvancedConfiguration';
import MonthControlBox from './MonthControlBox';

type CalendarProps = {
  readOnly?: boolean;
};

const Calendar: React.FC<CalendarProps> = ({ readOnly }) => {
  const {
    events,
    savedEvents,
    dates,
    initialDate,
    shouldApplyBeginEndDates,
    clearSelectedDates,
    bindDateCellControlRef,
    clearDateCellControlRef,
    setShouldApplyBeginEndDates,
    clearEvents,
  } = useContext(CalendarContext);

  useEffect(() => {
    return () => {
      clearDateCellControlRef();
      clearEvents();
    };
  }, []);

  if (!initialDate) {
    return <S.Layout>상위 요소에 CalendarProvider가 없습니다.</S.Layout>;
  }

  return (
    <S.Layout>
      {readOnly || (
        <>
          <button
            type="button"
            onClick={() => {
              clearSelectedDates();
            }}
            css={css`
              font-size: 0.75rem;
            `}
          >
            선택 해제
          </button>
          <div
            css={css`
              padding: 0.75rem;
              display: flex;
              flex-direction: column;
              gap: 0.5rem;
              font-size: 0.75rem;
            `}
          >
            <Toggle
              defaultChecked={shouldApplyBeginEndDates}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setShouldApplyBeginEndDates(e.target.checked);
              }}
            >
              요일 적용 기간 설정
            </Toggle>
            <AdvancedConfiguration />
          </div>
        </>
      )}
      <MonthControlBox />
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
            event={
              events.find(
                (event) => event.date === dateToFormattedString(date)
              ) ||
              savedEvents?.find(
                (event) => event.date === dateToFormattedString(date)
              )
            }
            readOnly={readOnly}
          />
        ))}
      </S.CalendarBox>
    </S.Layout>
  );
};

export default Calendar;
